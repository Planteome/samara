package org.planteome.samara

import net.ruippeixotog.scalascraper.model.Document


object ScraperGrin extends Scraper with ResourceUtil {

  object Parser extends ParserGrin with NameFinderStatic

  override def scrape() = {
    println("taxon id\ttaxon name\tdescriptor id\tdescriptor name\tdescriptor definition\tmethod id\tmethod name\tobserved value\taccession id\taccession number\taccession name\tcollected from\tcitations")
    val cropIds: Iterable[Crop] = getCropIds()
    cropIds.foreach(cropId => {
      val accessionIds = getAccessionIds(Seq(cropId))
      accessionIds.toSeq.distinct.foreach(accessionId => {
        val obs = getObservationsForAccession(accessionId._1)

        obs.foreach(ob => {
          val taxon = ob.accession.detail.taxa.head
          val line = Seq(taxon.id, taxon.name,
            ob.descriptor.id, accessionId._2.name.getOrElse(""), ob.descriptor.definition.getOrElse(""),
            ob.method.id, ob.method.name.getOrElse(""),
            ob.value,
            ob.accession.id,
            ob.accession.detail.number,
            ob.accession.detail.name,
            ob.accession.detail.collectedFrom.getOrElse(""),
            ob.accession.detail.references.mkString("|"))
          println(line.mkString("\t"))
        })
      })
    })
  }

  def getCropIds(): Iterable[Crop] = {
    val doc: Document = get("https://npgsweb.ars-grin.gov/gringlobal/descriptors.aspx")
    Parser.parseCropIds(doc)
  }

  def getAccessionIds(cropIds: Iterable[Crop]): Iterable[(Int, Descriptor)] = {
    cropIds.foldLeft(Seq.empty[(Int, Descriptor)])((agg0, crop) => {
      val descriptorsForCropDoc = get(crop.descriptorsUrl)
      val descriptorsForCrop = Parser.parseAvailableDescriptorIdsForCropId(descriptorsForCropDoc)
      descriptorsForCrop.foldLeft(Seq.empty[(Int, Descriptor)])((agg1, descriptorForCrop) => {
        val detailsDoc = get(descriptorForCrop.detailsUrl)
        val descriptorMethods = Parser.parseAvailableMethodsForDescriptor(detailsDoc)
        descriptorMethods.foldLeft(Seq.empty[(Int, Descriptor)])((agg2, descriptorMethod) => {
          val accessionsDoc = get(descriptorMethod.accessionsUrl)
          val accessionIds = Parser.parseAvailableAccessionsForDescriptorAndMethod(accessionsDoc)
          agg2 ++ accessionIds.map((_, descriptorMethod.descriptor))
        }) ++ agg1
      }) ++ agg0
    })
  }

  def getObservationsForAccession(accessionId: Int): Iterable[Observation] = {
    val accessionDetailsPage = get(s"https://npgsweb.ars-grin.gov/gringlobal/AccessionDetail.aspx?id=$accessionId")
    val accessionObservationPage = get(s"https://npgsweb.ars-grin.gov/gringlobal/AccessionObservation.aspx?id=$accessionId")
    val observations = Parser.parseObservationsForAccession(accessionObservationPage)
    val details = Parser.parseTaxonInAccessionDetails(accessionDetailsPage)

    observations.map {
      case (descriptor, method, observedValue) => {
        val accession: Accession = Accession(id = accessionId, detail = details)
        Observation(descriptor = descriptor, method = method, value = observedValue, accession = accession)
      }
    }
  }
}
