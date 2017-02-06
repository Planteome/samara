package org.planteome.samara

import java.io.InputStream
import java.util.zip.GZIPInputStream

import scala.collection.immutable.HashMap
import scala.io.Source

case class TaxonMap(providedId: String, providedName: String, resolvedId: String, resolvedName: String)


trait NameFinderTaxonCache extends NameFinder {

  def reducedTaxonMap: Iterator[(String, List[Integer])] = {
    taxonMapLines
      .filter(s => s.contains("NCBI:") || s.contains("NCBITaxon:"))
      .flatMap(line => {
        val parts = line.split("\t", -1).toList
        if (parts.size > 3) {
          Some(TaxonMap(parts(0), parts(1), parts(2), parts(3)))
        } else {
          None
        }
      })
      .filter(map => (map.resolvedId startsWith "NCBI:") || (map.resolvedId startsWith "NCBITaxon:"))
      .map(entry => (entry.providedName, List(new Integer(entry.resolvedId.replace("NCBI:", "").replace("NCBITaxon:", "")))))
  }

  def taxonMapStream: InputStream = {
    new GZIPInputStream(Samara.getClass.getResourceAsStream("/org/eol/globi/taxon/taxonMap.tsv.gz"))
  }

  def resourceNames: Seq[String] = {
    Seq("/org/planteome/samara/apsnet/taxonMap.tsv", "/org/eol/globi/taxon/taxonMap.tsv.gz")
  }

  def taxonMapLines: Iterator[String] = {
    resourceNames
      .map(resourceName => {
        val is = Samara.getClass.getResourceAsStream(resourceName)
        val is2 = if (resourceName.endsWith("gz")) {
          new GZIPInputStream(is)
        } else {
          is
        }
        Source.fromInputStream(is2).getLines()
      })
      .reduce(_ ++ _)
  }

  lazy val taxonCacheNCBI: collection.Map[String, List[Integer]] = {
    Console.err.println("taxonCache building...")
    val taxonCache = reducedTaxonMap
      .foldLeft(HashMap[String, List[Integer]]()) {
        (agg, entry) => {
          val targetTaxonIds: List[Integer] = agg.getOrElse(entry._1, List())
          agg + (entry._1 -> (entry._2 ++ targetTaxonIds).distinct)
        }
      }
    Console.err.println("taxonCache ready.")
    taxonCache
  }

  def findNames(text: String): List[String] = {
    taxonCacheNCBI.get(text) match {
      case Some(ids) => ids.map(id => s"NCBITaxon:$id")
      case None => List("no:match")
    }
  }
}

