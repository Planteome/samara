package org.planteome.samara

import java.io.File

import net.ruippeixotog.scalascraper.model.Document
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._


object ScraperGrin extends Scraper with ResourceUtil {

  object Parser extends ParserGrin with NameFinderStatic

  def scrape() = {
    println("scientific name\tdescriptor id\tdescriptor definition\tobserved value\taccession id")
    val cropIds: Iterable[Crop] = getCropIds()
    cropIds.foreach(cropId => {
      val accessionIds = getAccessionIds(Seq(cropId))
      accessionIds.toSeq.distinct.foreach(accessionId => {
        val obs = getObservationsForAccession(accessionId)

        obs.foreach(ob => {
          val line = Seq(ob.scientificName, ob.descriptor.id, ob.descriptor.definition.getOrElse(""), ob.value, ob.accessionId)
          println(line.mkString("\t"))
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

  def getObservationsForAccession(firstAccessionId: Int): Iterable[Observation] = {
    val accessionDetailsPage = get(s"https://npgsweb.ars-grin.gov/gringlobal/AccessionDetail.aspx?id=$firstAccessionId")
    val accessionObservationPage = get(s"https://npgsweb.ars-grin.gov/gringlobal/AccessionObservation.aspx?id=$firstAccessionId")
    val observations = Parser.parseObservationsForAccession(accessionObservationPage)
    val taxonIds = Parser.parseTaxonIdInAccessionDetails(accessionDetailsPage)

    val taxonPage = get(s"https://npgsweb.ars-grin.gov/gringlobal/taxonomydetail.aspx?id=${
      taxonIds.head
    }")
    val (scientificName, taxa) = Parser.parseTaxonPage(taxonPage)

    observations.map {
      case (descriptor, methodId, observedValue) => {
        Observation(scientificName = scientificName, taxonPath = taxa, descriptor = descriptor, method = Method(id = methodId, descriptor = descriptor), value = observedValue, accessionId = firstAccessionId)
      }
    }
  }
}
