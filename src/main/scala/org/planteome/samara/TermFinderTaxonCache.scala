package org.planteome.samara

import java.io.InputStream
import java.util.zip.GZIPInputStream

import scala.collection.immutable.HashMap
import scala.io.Source

case class TaxonMap(providedId: String, providedName: String, resolvedId: String, resolvedName: String)

case class TaxonMapCache(name: String
                         , lineFilter: (String) => Boolean
                         , prefixFilter: (TaxonMap) => Boolean
                         , prefixMap: (TaxonMap) => (String, List[Integer])
                         , expandId: (Integer) => String)

trait TermFinderTaxonCache extends TermFinder {

  def reducedTaxonMap: Iterator[(String, List[Integer])] = {
    taxonMapLines
      .filter(taxonMapCacheConfig.lineFilter)
      .flatMap(line => {
        val parts = line.split("\t", -1).toList
        if (parts.size > 3) {
          Some(TaxonMap(parts(0), parts(1), parts(2), parts(3)))
        } else {
          None
        }
      })
      .filter(taxonMapCacheConfig.prefixFilter)
      .map(taxonMapCacheConfig.prefixMap)
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

  def initTaxonCache: collection.Map[String, List[Integer]] = {
    Console.err.println("taxonCache building...")
    lazy val taxonCache = reducedTaxonMap
      .foldLeft(HashMap[String, List[Integer]]()) {
        (agg, entry) => {
          if (agg.contains(entry._1)) {
            agg
          } else {
            agg + (entry._1 -> entry._2)
          }
        }
      }
    Console.err.println("taxonCache ready.")
    taxonCache
  }

  def taxonMapCacheConfig: TaxonMapCache = {
    val name: String =  "NCBI"
    val lineFilter: (String) => Boolean = { (s: String) => s.contains("NCBI:") || s.contains("NCBITaxon:") }
    val prefixFilter: (TaxonMap) => Boolean = { (map: TaxonMap) => (map.resolvedId startsWith "NCBI:") || (map.resolvedId startsWith "NCBITaxon:") }
    val prefixMap: (TaxonMap) => (String, List[Integer]) = { (entry: TaxonMap) => (entry.providedName, List(new Integer(entry.resolvedId.replace("NCBI:", "").replace("NCBITaxon:", "").trim))) }
    val expandId: (Integer) => String = { (id: Integer) => s"NCBITaxon:$id" }
    TaxonMapCache(name = name, lineFilter = lineFilter, prefixFilter = prefixFilter, prefixMap = prefixMap, expandId = expandId)
  }

  lazy val taxonCache: collection.Map[String, List[Integer]] = initTaxonCache

  def findTerms(text: String): List[Term] = {
    taxonCache.get(text) match {
      case Some(ids) => ids.map(id => Term(text, taxonMapCacheConfig.expandId(id)))
      case None => List()
    }
  }
}

