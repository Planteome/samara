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

    ids.take(2) should contain(Descriptor(id = 115156, definition = Some("SOLUBLE SOLIDS (SOLSOLIDS) PERCENT SOLUBLE SOLIDS (AVERAGE REFRACTOMETER READINGS FROM 3 FRUITS AT FULL MATURITY)")))
    ids.take(2) should contain(Descriptor(id = 115159, definition = Some("Ploidy Level (PLOIDY) Ploidy level determined by nuclear DNA content using flow cytometry")))
  }

  "parsing descriptor detail page" should "generate a list of (descriptor, study / environment) id pairs" in {
    val doc: Document = parse("grin/descriptordetail.aspx")
    val methods: Iterable[MethodDescriptor] = ParserGrinStatic.parseAvailableMethodsForDescriptor(doc)

    methods should contain(MethodDescriptor(descriptor = Descriptor(115156), id = 492154))
    methods should contain(MethodDescriptor(descriptor = Descriptor(115156), id = 492159))
  }

  "parsing method assession page" should "generate a list of (descriptor, study / environment) id pairs" in {
    val doc: Document = parse("grin/methodaccession.aspx")
    val accessionIds: Iterable[Int] = ParserGrinStatic.parseAvailableAccessionsForDescriptorAndMethod(doc)

    accessionIds should contain(1011492)
    accessionIds should contain(1022529)
  }

  "parsing assession detail page" should "result in a taxon id" in {
    val doc: Document = parse("grin/AccessionDetail.aspx")
    val taxonIds: Iterable[Taxon] = ParserGrinStatic.parseTaxonInAccessionDetails(doc)
    taxonIds should contain(Taxon(id = 23257, name = "Malus platycarpa Rehder"))
  }

  "parsing assession observations page" should "list all observations for assession" in {
    val doc: Document = parse("grin/AccessionObservation.aspx")
    val observations: Iterable[(Descriptor, Method, String)] = ParserGrinStatic.parseObservationsForAccession(doc)

    val expectedDescriptorDefinition = "PERCENT SOLUBLE SOLIDS (AVERAGE REFRACTOMETER READINGS FROM 3 FRUITS AT FULL MATURITY)"
    observations should contain((Descriptor(id = 115156, definition = Some(expectedDescriptorDefinition)), Method(492154, Some("APPLE.MORPHOLOGIC.00")), "10"))
    observations should contain((Descriptor(id = 115134, definition = Some("DEFINITION OF USUAL METHOD OF CONSUMPTION.")), Method(492154, Some("APPLE.MORPHOLOGIC.00")), "6,6 - OTHER USE (ORNAMENTAL, ROOTSTOCK, GERMPLASM)"))
  }

  "parsing taxon page" should "return a taxon path" in {
    val doc: Document = parse("grin/taxonomydetail.aspx")
    val (scientificName, ranks) = ParserGrinStatic.parseTaxonPage(doc)

    scientificName should be("Malus platycarpa Rehder")
    ranks should contain(Taxon(name = "Malus", rank = Some("genus"), id = 7215))
    ranks should contain(Taxon(name = "Rosaceae", rank = Some("family"), id = 972))
  }

}