package org.planteome.samara

import java.io.File

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._
import net.ruippeixotog.scalascraper.model.{Element, Document}
import org.scalatest._

import scala.util.matching.Regex


object GRINParserStatic extends NameFinderStatic {
  def parseCropIds(doc: Document): Iterable[Int] = {
    val ids = doc >> elements(".ddl_crops") >> attrs("value")("option")
    ids.map(Integer.parseInt(_)).filter(_ > 0)
  }

  def parseAvailableDescriptorIds(doc: Document): Iterable[Int] = {
    val urls: Iterable[String] = doc >> elements("li") >> attrs("href")("a")
    extractIdsFromUrls(urls, """(descriptordetail.aspx\?id=)(\d+)""".r)
  }

  def parseAvailableMethodsForDescriptor(doc: Document): Iterable[(Int, Int)] = {
    val urls: Iterable[String] = doc >> elements("li") >> attrs("href")("a")
    val methodDetails: Regex = """(methodaccession.aspx\?id1=)(\d+)(&id2=)(\d+)""".r
    urls
      .flatMap(
        methodDetails findFirstIn _ match {
          case Some(methodDetails(_, descriptorId, _, methodId)) => {
            Some((Integer.parseInt(descriptorId), Integer.parseInt(methodId)))
          }
          case _ => None
        })
  }

  def parseAvailableAccessionsForDescriptorAndMethod(doc: Document): Iterable[Int] = {
    val urls: Iterable[String] = doc >> elements("td") >> attrs("href")("a")
    extractIdsFromUrls(urls, """(AccessionDetail.aspx\?id=)(\d+)""".r)
  }

  def parseTaxonIdInAccessionDetails(doc: Document): Iterable[Int] = {
    val urls: Iterable[String] = doc >> elements("b") >> attrs("href")("a")
    extractIdsFromUrls(urls, """(taxonomydetail.aspx\?id=)(\d+)""".r)
  }

  def extractIdsFromUrls(urls: Iterable[String], regex: Regex): Iterable[Int] = {
    urls
      .flatMap(
        regex findFirstIn _ match {
          case Some(regex(_, someId)) => {
            Some(Integer.parseInt(someId))
          }
          case _ => None
        })
  }

  def parseObservationsForAccession(doc: Document): Iterable[(Int, Int, String)] = {
    val table = doc >> element("div#content") >> elements("table")
    val descriptorUrls = table >> elements("td:nth-of-type(4n-3)") >> attrs("href")("a")
    val descriptorIds = extractIdsFromUrls(descriptorUrls, """(descriptordetail.aspx\?id=)(\d+)""".r)

    val methodUrls = table >> elements("td:nth-of-type(4n-1)") >> attrs("href")("a")
    val methodIds = extractIdsFromUrls(methodUrls, """(method.aspx\?id=)(\d+)""".r)

    val values = table >> texts("td:nth-of-type(4n-2)")

    (descriptorIds, methodIds, values).zipped.toList
  }

}

class GRINParser$Test extends FlatSpec with Matchers with NameFinderStatic with ParseTestUtil {

  "parsing descriptors page" should "generate a list of species ids" in {
    val doc: Document = parse("grin/descriptors.aspx")
    val ids: Iterable[Int] = GRINParserStatic.parseCropIds(doc)

    ids should not contain (-1)
    ids should contain(115)
    ids should contain(265)
  }

  "parsing descriptor page" should "generate a list of available descriptors" in {
    val doc: Document = parse("grin/cropdetail.aspx")
    val ids: Iterable[Int] = GRINParserStatic.parseAvailableDescriptorIds(doc)

    ids should contain(115156)
    ids should contain(115159)
  }

  "parsing descriptor detail page" should "generate a list of (descriptor, study / environment) id pairs" in {
    val doc: Document = parse("grin/descriptordetail.aspx")
    val idPairs: Iterable[(Int, Int)] = GRINParserStatic.parseAvailableMethodsForDescriptor(doc)

    idPairs should contain((115156, 492154))
    idPairs should contain((115156, 492159))
  }

  "parsing method assession page" should "generate a list of (descriptor, study / environment) id pairs" in {
    val doc: Document = parse("grin/methodaccession.aspx")
    val acessions: Iterable[Int] = GRINParserStatic.parseAvailableAccessionsForDescriptorAndMethod(doc)

    acessions should contain(1011492)
    acessions should contain(1022529)
  }

  "parsing assession detail page" should "result in a taxon id" in {
    val doc: Document = parse("grin/AccessionDetail.aspx")
    val taxonIds: Iterable[Int] = GRINParserStatic.parseTaxonIdInAccessionDetails(doc)
    taxonIds should contain(23257)
  }

  "parsing assession observations page" should "list all observations for assession" in {
    val doc: Document = parse("grin/AccessionObservation.aspx")
    val observations: Iterable[(Int, Int, String)] = GRINParserStatic.parseObservationsForAccession(doc)

    observations should contain((115156, 492154, "10"))
    observations should contain((115134, 492154, "6,6 - OTHER USE (ORNAMENTAL, ROOTSTOCK, GERMPLASM)"))
  }

}