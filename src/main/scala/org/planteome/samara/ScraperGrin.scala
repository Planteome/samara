package org.planteome.samara

import net.ruippeixotog.scalascraper.model.Document

import scala.util.{Failure, Success}


object ScraperGrin extends Scraper with ResourceUtil with IdFinderTaxonCache {

  object Parser extends ParserGrin

  override def scrape() = {
    println("verbatim_taxon_id\tverbatim_taxon_name\tresolved_taxon_id\tdescriptor_id\tdescriptor_name\tdescriptor_definition\tmethod_id\tmethod_name\tobserved_value\taccession_id\taccession_number\taccession_name\tcollected_from\tcitations")
    val cropIds: Iterable[Crop] = getCropIds()
    cropIds.foreach(cropId => {
      val accessionIds = getAccessionIds(Seq(cropId))
      accessionIds.toSeq.distinct.foreach(accessionId => {
        val obs = getObservationsForAccession(accessionId)

        obs.foreach(ob => {
          val taxon = ob.accession.detail.taxa.head
          val taxonId = s"GRINTaxon:${taxon.id}"
          val lines = findIds(taxonId).map(resolvedTaxonId => {
            Seq(taxonId, taxon.name, resolvedTaxonId,
              s"GRINDesc:${ob.descriptor.id}", ob.descriptor.name.getOrElse(""), ob.descriptor.definition.getOrElse(""),
              s"GRINMethod:${ob.method.id}", ob.method.name.getOrElse(""),
              ob.value,
              s"GRINAccess:${ob.accession.id}",
              ob.accession.detail.number,
              ob.accession.detail.name,
              ob.accession.detail.collectedFrom.getOrElse(""),
              ob.accession.detail.references.mkString("|"))
          })
          lines.foreach(line => println(line.mkString("\t")))
        })
      })
    })
  }

  def getCropIds(): Iterable[Crop] = {
    getTry("https://npgsweb.ars-grin.gov/gringlobal/descriptors.aspx") match {
      case Success(doc) => Parser.parseCropIds(doc)
      case Failure(e) => Seq.empty[Crop]
    }
  }

  def getAccessionIds(crops: Iterable[Crop]): Iterable[Int] = {
    def foldLeftAccessionIds(agg2: Seq[Int], accessionsDoc: Document): Seq[Int] = {
      val accessionIds = Parser.parseAvailableAccessionsForDescriptorAndMethod(accessionsDoc)
      agg2 ++ accessionIds
    }
    def foldLeftDetails(agg1: Seq[Int], detailsDoc: Document): Seq[Int] = {
      val descriptorMethods = Parser.parseAvailableMethodsForDescriptor(detailsDoc)
      descriptorMethods.foldLeft(Seq.empty[Int])((agg2, descriptorMethod) => {
        getTry(descriptorMethod.accessionsUrl) match {
          case Success(accessionsDoc) => foldLeftAccessionIds(agg2, accessionsDoc)
          case Failure(e) => Seq.empty[Int]
        }
      }) ++ agg1
    }
    def foldLeftDescriptors(agg0: Seq[Int], descriptorsForCropDoc: Document): Seq[Int] = {
      val descriptorsForCrop = Parser.parseAvailableDescriptorIdsForCropId(descriptorsForCropDoc)
      descriptorsForCrop.foldLeft(Seq.empty[Int])((agg1, descriptorForCrop) => {
        getTry(descriptorForCrop.detailsUrl) match {
          case Success(detailsDoc) => foldLeftDetails(agg1, detailsDoc)
          case Failure(e) => Seq.empty[Int]

        }
      }) ++ agg0
    }
    crops.foldLeft(Seq.empty[Int])((agg0, crop) => {
      getTry(crop.descriptorsUrl) match {
        case Success(descriptorDoc) => foldLeftDescriptors(agg0, descriptorDoc)
        case Failure(e) => Seq.empty[Int]
      }
    })
  }

  def getObservationsForAccession(accessionId: Int): Iterable[Observation] = {
    def detailsForAccession(accessionDetailsPage: Document): Iterable[Observation] = {
      val details = Parser.parseTaxonInAccessionDetails(accessionDetailsPage)
      details match {
        case Some(accessionDetails) => {
          val accession: Accession = Accession(id = accessionId, detail = accessionDetails)
          getTry(s"https://npgsweb.ars-grin.gov/gringlobal/AccessionObservation.aspx?id=$accessionId") match {
            case Success(accessionObservationPage) =>
              val observations = Parser.parseObservationsForAccession(accessionObservationPage)
              observations.map {
                case (descriptor, method, observedValue) =>
                  Observation(descriptor = descriptor, method = method, value = observedValue, accession = accession)
              }
            case Failure(e) => Seq.empty[Observation]
          }
        }
        case None => {
          Console.err.println(s"failed to retrieve information for accession id [$accessionId].")
          List()
        }
      }
    }

    getTry(s"https://npgsweb.ars-grin.gov/gringlobal/AccessionDetail.aspx?id=$accessionId") match {
      case Success(accessionDetailsPage) => detailsForAccession(accessionDetailsPage)
      case Failure(e) => Seq.empty[Observation]
    }
  }
}
