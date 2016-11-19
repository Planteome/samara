package org.planteome.samara

import java.io.InputStream
import java.net.URL

import scala.io.Source
import scala.xml.{NodeSeq, XML}

trait NCBITaxonFinder extends IdFinder {

  lazy val idMap = buildIdMap(findNCBITaxonIdsWithGRINTaxonLinkOut)

  override def findId(id: Integer): Option[Int] = {
    idMap.get(id)
  }

  def buildIdMap(ids: Seq[Int]): Map[Int, Int] = {
    Console.err.println("(GRIN Taxon->NCBI Taxon) building...")
    val aMap = ids
      .flatMap { ncbiId => findLinkOutForNCBITaxonId(ncbiId).map((_, ncbiId)) }
      .toMap
    Console.err.println("(GRIN Taxon->NCBI Taxon) done.")
    aMap
  }


  def findNCBITaxonIdsWithGRINTaxonLinkOut: Seq[Int] = {
    val ids = Source.fromInputStream(getClass.getResourceAsStream("ncbiTaxonIdsWithGRINLinkOut.txt"), "UTF8")
    ids.getLines().map(Integer.parseInt).toSeq
  }

  def findLinkOutForNCBITaxonId(ncbiTaxonId: Int, linkOutProviderNameAbbreviation: String = "GRINTAX"): Seq[Int] = {
    val url = s"https://eutils.ncbi.nlm.nih.gov/entrez/eutils/elink.fcgi?dbfrom=taxonomy&id=$ncbiTaxonId&cmd=llinks&retmode=xml&holding=" + linkOutProviderNameAbbreviation
    parseGRINTaxonIds(new URL(url).openStream())
  }

  def parseGRINTaxonIds(is: InputStream): Seq[Int] = {
    val xmlDoc = scala.xml.Source.fromInputStream(is)
    val grinTaxonUrls: NodeSeq = XML.load(xmlDoc) \\ "ObjUrl" \ "Url"
    grinTaxonUrls.map(_.text).flatMap(extractIdFromUrl)
  }

  def extractIdFromUrl(url: String): Option[Int] = {
    val grinUrl = ".*taxonomy(detail|genus|family)\\.aspx\\?([0-9]+)".r
    url match {
      case grinUrl("detail", id) => Some(Integer.parseInt(id))
      case _ => None
    }
  }

}

