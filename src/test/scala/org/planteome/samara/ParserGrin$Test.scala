package org.planteome.samara

import java.io.File

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._
import net.ruippeixotog.scalascraper.model.{Element, Document}
import org.scalatest._

import scala.util.matching.Regex

object ParserGrinStatic extends ParserGrin with NameFinderStatic

class ParserGrin$Test extends FlatSpec with Matchers with NameFinderStatic with ParseTestUtil {

  "parsing descriptors page" should "generate a list of species ids" in {
    val doc: Document = parse("grin/descriptors.aspx")
    val ids: Iterable[Crop] = ParserGrinStatic.parseCropIds(doc)

    ids should not contain (-1)
    ids should contain(Crop(115))
    ids should contain(Crop(265))
  }

  "parsing descriptor page" should "generate a list of available descriptors" in {
    val doc: Document = parse("grin/cropdetail.aspx")
    val ids: Iterable[Descriptor] = ParserGrinStatic.parseAvailableDescriptorIdsForCropId(doc)

    ids should contain(Descriptor(115156))
    ids should contain(Descriptor(115159))
  }

  "parsing descriptor detail page" should "generate a list of (descriptor, study / environment) id pairs" in {
    val doc: Document = parse("grin/descriptordetail.aspx")
    val methods: Iterable[Method] = ParserGrinStatic.parseAvailableMethodsForDescriptor(doc)

    methods should contain(Method(descriptor = Descriptor(115156), id = 492154))
    methods should contain(Method(descriptor = Descriptor(115156), id = 492159))
  }

  "parsing method assession page" should "generate a list of (descriptor, study / environment) id pairs" in {
    val doc: Document = parse("grin/methodaccession.aspx")
    val accessionIds: Iterable[Int] = ParserGrinStatic.parseAvailableAccessionsForDescriptorAndMethod(doc)

    accessionIds should contain(1011492)
    accessionIds should contain(1022529)
  }

  "parsing assession detail page" should "result in a taxon id" in {
    val doc: Document = parse("grin/AccessionDetail.aspx")
    val taxonIds: Iterable[Int] = ParserGrinStatic.parseTaxonIdInAccessionDetails(doc)
    taxonIds should contain(23257)
  }

  "parsing assession observations page" should "list all observations for assession" in {
    val doc: Document = parse("grin/AccessionObservation.aspx")
    val observations: Iterable[(Int, Int, String)] = ParserGrinStatic.parseObservationsForAccession(doc)

    observations should contain((115156, 492154, "10"))
    observations should contain((115134, 492154, "6,6 - OTHER USE (ORNAMENTAL, ROOTSTOCK, GERMPLASM)"))
  }

  "parsing taxon page" should "return a taxon path" in {
    val doc: Document = parse("grin/taxonomydetail.aspx")
    val (scientificName, ranks) = ParserGrinStatic.parseTaxonPage(doc)

    scientificName should be("Malus platycarpa Rehder")
    ranks should contain(Taxon(name = "Malus", rank = "genus", id = 7215))
    ranks should contain(Taxon(name = "Rosaceae", rank = "family", id = 972))
  }

  val apple = Crop(115)
  val wheat = Crop(65)

  "parser" should "enable retrieval of crop ids" in {
    val doc: Document = get("https://npgsweb.ars-grin.gov/gringlobal/descriptors.aspx")
    val cropIds = ParserGrinStatic.parseCropIds(doc)
    cropIds should contain(apple)
    cropIds should contain(wheat)
  }

  "parser" should "be able to retrieve accessors for apple and wheat crops" in {
    val accessionIds = Seq(apple, wheat).foldLeft(Seq.empty[Int])((agg0, crop) => {
      val descriptorsForCropDoc = get(crop.descriptorsUrl)
      val descriptorsForCrop = ParserGrinStatic.parseAvailableDescriptorIdsForCropId(descriptorsForCropDoc)
      descriptorsForCrop.foldLeft(Seq.empty[Int])((agg1, descriptorForCrop) => {
        val detailsDoc = get(descriptorForCrop.detailsUrl)
        val descriptorMethods = ParserGrinStatic.parseAvailableMethodsForDescriptor(detailsDoc)
        descriptorMethods.foldLeft(Seq.empty[Int])((agg2, descriptorMethod) => {
          val accessionsDoc = get(descriptorMethod.accessionsUrl)
          val accessionIds = ParserGrinStatic.parseAvailableAccessionsForDescriptorAndMethod(accessionsDoc)
          agg2 ++ accessionIds
        }) ++ agg1
      }) ++ agg0

    })

    println(s"counted [${accessionIds.size}] accessors")
    println(s"counted [${accessionIds.distinct.size}] distinct accessors")
    accessionIds.distinct.foreach(println)

    accessionIds.headOption match {
      case Some(firstAccessionId) => {
        val accessionDetailsPage = get(s"https://npgsweb.ars-grin.gov/gringlobal/AccessionDetail.aspx?id=$firstAccessionId")
        val observations = ParserGrinStatic.parseObservationsForAccession(accessionDetailsPage)
        val taxonIds = ParserGrinStatic.parseTaxonIdInAccessionDetails(accessionDetailsPage)

        val taxonPage = get(s"https://npgsweb.ars-grin.gov/gringlobal/taxonomydetail.aspx?id=${
          taxonIds.head
        }.")
        val (scientificName, taxa) = ParserGrinStatic.parseTaxonPage(taxonPage)

        println(s"printing some observations for accession $firstAccessionId")
        observations
          .map {
            case (descriptorId, methodId, observedValue) => {
              Observation(scientificName = scientificName, taxonPath = taxa, descriptor = Descriptor(descriptorId), method = Method(id = methodId, descriptor = Descriptor(descriptorId)), value = observedValue, id = firstAccessionId)
            }
          }
          .foreach(println)
      }
      case None => {
        fail("expected at least one accessor, but found none")
      }
    }

  }

}