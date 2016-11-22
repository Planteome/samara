package org.planteome.samara

import java.io.InputStream
import java.util.zip.GZIPInputStream

import scala.collection.immutable.{HashMap}
import scala.io.Source

trait IdFinderTaxonCache extends IdFinder {

  def taxonMapStream: InputStream = {
    new GZIPInputStream(Samara.getClass.getResourceAsStream("/org/planteome/samara/taxonMap.tsv.gz"))
  }

  def reducedTaxonMap: Iterator[(String, String)] = {
    taxonMapLines
      .flatMap(line => {
        val parts = line.split("\t", -1).toList
        if (parts.size > 1) {
          Some((parts(0), parts(1)))
        } else {
          None
        }
      })
      .flatMap(entry => Seq((entry._1, entry._2), (entry._2, entry._1)))
  }


  def taxonMapLines: Iterator[String] = {
    Source.fromInputStream(taxonMapStream)
      .getLines()
  }

  lazy val taxonCacheNCBI: collection.Map[String, List[String]] = {
    Console.err.println("taxonCache building...")
    val taxonCache = reducedTaxonMap
      .foldLeft(new HashMap[String, List[String]]()) {
        (agg, entry) => {
          val targetTaxonIds: List[String] = agg.getOrElse(entry._1, List())
          agg + (entry._1 -> (List(entry._2) ++ targetTaxonIds).distinct)
        }
      }
    Console.err.println("taxonCache ready.")
    taxonCache
  }

  override def findIds(id: String): List[String] = {
    taxonCacheNCBI.getOrElse(id, List("no:match"))
  }
}

