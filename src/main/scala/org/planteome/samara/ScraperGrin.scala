package org.planteome.samara

import java.io.File

import net.ruippeixotog.scalascraper.model.Document
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._


object ScraperGrin extends Scraper with ResourceUtil {

  object Parser extends ParserGrin with NameFinderStatic

  override def scrape() = {
    println("taxon id\ttaxon name\tdescriptor id\tdescriptor definition\tmethod id\tmethod name\tobserved value\taccession id")
    val cropIds: Iterable[Crop] = getCropIds()
    cropIds.foreach(cropId => {
      val accessionIds = getAccessionIds(Seq(cropId))
      accessionIds.toSeq.distinct.foreach(accessionId => {
        val obs = getObservationsForAccession(accessionId)

        obs.foreach(ob => {
          val line = Seq(ob.taxon.id, ob.taxon.name, ob.descriptor.id, ob.descriptor.definition.getOrElse(""), ob.method.id, ob.method.name.getOrElse(""), ob.value, ob.accessionId)
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

  def getObservationsForAccession(accessionId: Int): Iterable[Observation] = {
    val accessionDetailsPage = get(s"https://npgsweb.ars-grin.gov/gringlobal/AccessionDetail.aspx?id=$accessionId")
    val accessionObservationPage = get(s"https://npgsweb.ars-grin.gov/gringlobal/AccessionObservation.aspx?id=$accessionId")
    val observations = Parser.parseObservationsForAccession(accessionObservationPage)
    val taxa = Parser.parseTaxonInAccessionDetails(accessionDetailsPage)

    observations.map {
      case (descriptor, method, observedValue) => {
        Observation(taxa.head, descriptor = descriptor, method = method, value = observedValue, accessionId = accessionId)
      }
    }
  }
}
