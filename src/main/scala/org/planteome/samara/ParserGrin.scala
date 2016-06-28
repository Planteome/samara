package org.planteome.samara

import net.ruippeixotog.scalascraper.scraper.ContentExtractors._
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._
import net.ruippeixotog.scalascraper.model.{Element, Document}


import scala.util.matching.Regex

abstract class GRINTerm

case class MethodDescriptor(id: Int, descriptor: Descriptor) extends GRINTerm {
  def accessionsUrl = s"https://npgsweb.ars-grin.gov/gringlobal/methodaccession.aspx?id1=${descriptor.id}&id2=${id}"
}

case class Method(id: Int, name: Option[String]) extends GRINTerm

case class Crop(id: Int) extends GRINTerm {
  def descriptorsUrl = s"https://npgsweb.ars-grin.gov/gringlobal/cropdetail.aspx?type=descriptor&id=$id"
}

case class Taxon(name: String, id: Int, rank: Option[String] = None) extends GRINTerm

case class Descriptor(id: Int, definition: Option[String] = None) extends GRINTerm {
  def detailsUrl = s"https://npgsweb.ars-grin.gov/gringlobal/descriptordetail.aspx?id=$id"
}

case class Accession(id: Int) extends GRINTerm

case class Observation(taxon: Taxon, taxonPath: Option[Iterable[Taxon]] = None, descriptor: Descriptor, method: Method, value: String, accessionId: Int) extends GRINTerm

abstract class ParserGrin extends NameFinder with Scrubber {
  def parseCropIds(doc: Document): Iterable[Crop] = {
    val ids = doc >> elements(".ddl_crops") >> attrs("value")("option")
    ids.map(Integer.parseInt(_)).filter(_ > 0).map(Crop(_))
  }

  def parseAvailableDescriptorIdsForCropId(doc: Document): Iterable[Descriptor] = {
    val urls: Iterable[String] = doc >> elements("li") >> attrs("href")("a")
    val descriptorIds = extractIdsFromUrls(urls, """(descriptordetail.aspx\?id=)(\d+)""".r)
    val labels: Iterable[String] = doc >> texts("li")
    descriptorIds
      .zip(labels.map(scrub))
      .map {
        case (descriptorId, descriptorText) => Descriptor(id = descriptorId, definition = Some(descriptorText))
      }
  }

  def parseAvailableMethodsForDescriptor(doc: Document): Iterable[MethodDescriptor] = {
    val urls: Iterable[String] = doc >> elements("li") >> attrs("href")("a")
    val methodDetails: Regex = """(methodaccession.aspx\?id1=)(\d+)(&id2=)(\d+)""".r
    urls
      .flatMap(
        methodDetails findFirstIn _ match {
          case Some(methodDetails(_, descriptorId, _, methodId)) => {
            Some(MethodDescriptor(descriptor = Descriptor(id = Integer.parseInt(descriptorId)), id = Integer.parseInt(methodId)))
          }
          case _ => None
        })
  }

  def parseAvailableAccessionsForDescriptorAndMethod(doc: Document): Iterable[Int] = {
    val urls: Iterable[String] = doc >> elements("td") >> attrs("href")("a")
    extractIdsFromUrls(urls, """(AccessionDetail.aspx\?id=)(\d+)""".r)
  }

  def parseTaxonInAccessionDetails(doc: Document): Iterable[Taxon] = {
    val taxonomyDetailRegex: Regex = """(taxonomydetail.aspx\?id=)(\d+)""".r
    val aElem = doc >> elements("b")
    val names: Iterable[Element] = aElem >> elements("a")
    val taxonElems = names.filter { nameElem => taxonomyDetailRegex.findFirstMatchIn(nameElem.attr("href")).isDefined }
    taxonElems.flatMap {
      elem => {
        taxonomyDetailRegex.findFirstIn(elem.attr("href")) match {
          case Some(taxonomyDetailRegex(_, someId)) => {
            Some(Taxon(id = Integer.parseInt(someId), name = elem.text))
          }
          case _ => None
        }
      }
    }
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

  def parseObservationsForAccession(doc: Document): Iterable[(Descriptor, Method, String)] = {
    val table = doc >> element("div#content") >> elements("table")
    val descriptorElems = table >> elements("td:nth-of-type(4n-3)")
    val descriptorUrls = descriptorElems >> attrs("href")("a")
    val descriptorIds = extractIdsFromUrls(descriptorUrls, """(descriptordetail.aspx\?id=)(\d+)""".r)
    val descriptorTitles = descriptorElems >> attrs("title")("a")
    val descriptors = descriptorIds.zip(descriptorTitles).map {
      case (descriptorId, descriptorTitle) => Descriptor(id = descriptorId, definition = Some(descriptorTitle))
    }

    val methodElems = table >> elements("td:nth-of-type(4n-1)")
    val methodNames = methodElems >> texts("a")
    val methodUrls = methodElems >> attrs("href")("a")
    val methodIds = extractIdsFromUrls(methodUrls, """(method.aspx\?id=)(\d+)""".r)

    val methods = methodIds.zip(methodNames).map {
      case (methodId, methodName) => Method(id = methodId, name = Some(methodName))
    }

    val values = table >> texts("td:nth-of-type(4n-2)")

    (descriptors, methods, values).zipped.toList
  }

  def parseTaxonPage(doc: Document): (String, Iterable[Taxon]) = {
    val table = doc >> element("table.detail")

    val h1 = table >> text("h1")

    val scientificName = scrub(h1.replace("Taxon:", ""))

    val (names, urls) = table >> element("table.grid") >>(texts("a"), attrs("href")("a"))

    val ranks = extractTaxa(names, urls, """(taxonomy)(\w+)(.aspx\?id=)(\d+)""".r)
    val familyRanks = extractTaxa(names, urls, """(taxonomyfamily.aspx\?type=)(\w+)(&id=)(\d+)""".r)

    (scientificName, ranks ++ familyRanks)
  }

  def extractTaxa(names: Iterable[String], urls: Iterable[String], regex: Regex): Iterable[Taxon] = {
    val familyRanks = names.zip(urls)
      .flatMap { case (name, url) =>
        regex findFirstIn (url) match {
          case Some(regex(_, rank, _, someId)) => {
            Some(Taxon(rank = Some(rank), id = Integer.parseInt(someId), name = name))
          }
          case _ => None
        }
      }
    familyRanks
  }
}
