package org.planteome.samara

import net.ruippeixotog.scalascraper.model.Document


object ScraperGrin extends Scraper with ResourceUtil with NameFinderTaxonCache {

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
          val lines = findNames(taxon.name).map(resolvedTaxonId => {
            Seq(s"GRINTaxon:${taxon.id}", taxon.name, resolvedTaxonId,
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
    val doc: Document = get("https://npgsweb.ars-grin.gov/gringlobal/descriptors.aspx")
    Parser.parseCropIds(doc)
  }

  def getAccessionIds(cropIds: Iterable[Crop]): Iterable[Int] = {
    cropIds.foldLeft(Seq.empty[Int])((agg0, crop) => {
      val descriptorsForCropDoc = get(crop.descriptorsUrl)
      val descriptorsForCrop = Parser.parseAvailableDescriptorIdsForCropId(descriptorsForCropDoc)
      descriptorsForCrop.foldLeft(Seq.empty[Int])((agg1, descriptorForCrop) => {
        val detailsDoc = get(descriptorForCrop.detailsUrl)
        val descriptorMethods = Parser.parseAvailableMethodsForDescriptor(detailsDoc)
        descriptorMethods.foldLeft(Seq.empty[Int])((agg2, descriptorMethod) => {
          val accessionsDoc = get(descriptorMethod.accessionsUrl)
          val accessionIds = Parser.parseAvailableAccessionsForDescriptorAndMethod(accessionsDoc)
          agg2 ++ accessionIds
        }) ++ agg1
      }) ++ agg0
    })
  }

  def getObservationsForAccession(accessionId: Int): Iterable[Observation] = {
    val accessionDetailsPage = get(s"https://npgsweb.ars-grin.gov/gringlobal/AccessionDetail.aspx?id=$accessionId")
    val details = Parser.parseTaxonInAccessionDetails(accessionDetailsPage)
    details match {
      case Some(accessionDetails) => {
        val accession: Accession = Accession(id = accessionId, detail = accessionDetails)
        val accessionObservationPage = get(s"https://npgsweb.ars-grin.gov/gringlobal/AccessionObservation.aspx?id=$accessionId")
        val observations = Parser.parseObservationsForAccession(accessionObservationPage)
        observations.map {
          case (descriptor, method, observedValue) => {
            Observation(descriptor = descriptor, method = method, value = observedValue, accession = accession)
          }
        }
      }
      case None => {
        Console.err.println(s"failed to retrieve information for accession id [$accessionId].")
        List()
      }
    }

  }
}
