package org.planteome.samara

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._
import net.ruippeixotog.scalascraper.model.{Document, Element}


import scala.util.matching.Regex

abstract class GRINTerm

case class MethodDescriptor(id: Int, descriptor: Descriptor) extends GRINTerm {
  def accessionsUrl = s"https://npgsweb.ars-grin.gov/gringlobal/methodaccession.aspx?id1=${descriptor.id}&id2=$id"
}

case class Method(id: Int, name: Option[String]) extends GRINTerm

case class Crop(id: Int) extends GRINTerm {
  def descriptorsUrl = s"https://npgsweb.ars-grin.gov/gringlobal/cropdetail.aspx?type=descriptor&id=$id"
}

case class Taxon(name: String, id: Int, rank: Option[String] = None) extends GRINTerm

case class Descriptor(id: Int, definition: Option[String] = None, name: Option[String] = None) extends GRINTerm {
  def detailsUrl = s"https://npgsweb.ars-grin.gov/gringlobal/descriptordetail.aspx?id=$id"
}

case class Accession(id: Int, details: AccessionDetails) extends GRINTerm

case class AccessionDetails(name: String, number: String, collectedFrom: Option[String], taxa: Iterable[Taxon], references: Iterable[String]) extends GRINTerm

case class Observation(descriptor: Descriptor, method: Method, value: String, accession: Accession) extends GRINTerm

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
    val descriptorName = doc >> element("table.detail") >> text("h1")

    val urls: Iterable[String] = doc >> elements("li") >> attrs("href")("a")
    val methodDetails: Regex = """(methodaccession.aspx\?id1=)(\d+)(&id2=)(\d+)""".r
    urls
      .flatMap(
        methodDetails findFirstIn _ match {
          case Some(methodDetails(_, descriptorId, _, methodId)) => {
            Some(MethodDescriptor(descriptor = Descriptor(id = Integer.parseInt(descriptorId),
              name = Some(descriptorName.replace("Descriptor: ", ""))),
              id = Integer.parseInt(methodId)))
          }
          case _ => None
        })
  }

  def parseAvailableAccessionsForDescriptorAndMethod(doc: Document): Iterable[Int] = {
    val urls: Iterable[String] = doc >> elements("td") >> attrs("href")("a")
    extractIdsFromUrls(urls, """(AccessionDetail.aspx\?id=)(\d+)""".r)
  }

  def parseTaxonInAccessionDetails(doc: Document): AccessionDetails = {
    val taxonomyDetailRegex: Regex = """(taxonomydetail.aspx\?id=)(\d+)""".r
    val accessionNumber = doc >> text("h1")
    val accessionName = doc >> text("div#main-wrapper > b")
    val headers = doc >> elements("th")
    val collectedFromRow = headers
      .filter {
        _.text == "Collected from:"
      }
      .flatMap {
        _.parent
      }

    val collectedFrom = collectedFromRow.headOption match {
      case Some(row) => Some(row >> text("td"))
      case None => None
    }


    val referenceRegex: Regex = """(Reference: )(.*)((Comment:)(.*))?$""".r
    val listItems = doc >> elements("li")
    val references = listItems.flatMap {
      elem => extractReference(elem.text)
    }

    val aElem = doc >> elements("b")
    val names: Iterable[Element] = aElem >> elements("a")
    val taxonElems = names.filter { nameElem => taxonomyDetailRegex.findFirstMatchIn(nameElem.attr("href")).isDefined }
    val taxa = taxonElems.flatMap {
      elem => {
        taxonomyDetailRegex.findFirstIn(elem.attr("href")) match {
          case Some(taxonomyDetailRegex(_, someId)) => {
            Some(Taxon(id = Integer.parseInt(someId), name = elem.text))
          }
          case _ => None
        }
      }
    }
    AccessionDetails(number = accessionNumber,
      name = accessionName,
      collectedFrom = collectedFrom,
      taxa = taxa,
      references = references)
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

  def extractReference(citationText: String): Option[String] = {
    val referenceRegex: Regex = """(Reference: )(.*)""".r
    val commentRegex: Regex = """(.*)(Comment:)(.*)""".r
    citationText match {
      case referenceRegex(_, ref) => ref match {
        case commentRegex(refNoCmt, _, _) => Some(refNoCmt.trim)
        case _ => Some(ref.trim)
      }
      case _ => None
    }
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
