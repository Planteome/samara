package org.planteome.samara

import java.io.{File, InputStream}
import java.util.zip.GZIPInputStream

import org.mapdb.{BTreeMap, DBMaker}
import scala.collection.JavaConverters._
import scala.collection.immutable.HashMap

import scala.collection.mutable
import scala.io.Source

case class TaxonMap(providedId: String, providedName: String, resolvedId: String, resolvedName: String)


trait NameFinderTaxonCache extends NameFinder {

  def taxonMapStream: InputStream = {
    new GZIPInputStream(Samara.getClass.getResourceAsStream("/org/eol/globi/taxon/taxonMap.tsv.gz"))
  }


  lazy val taxonCacheNCBI: Map[String, List[Integer]] = {
    println("taxonCache building...")
    val dbFile = File.createTempFile("mapdb", "temp")
    dbFile.deleteOnExit()

    val taxonCache = Source.fromInputStream(taxonMapStream)
      .getLines()
      .filter(_.contains("NCBI:"))
      .flatMap(line => {
        val parts = line.split("\t").toList
        if (parts.size > 3) {
          Some(TaxonMap(parts(0), parts(1), parts(2), parts(3)))
        } else {
          None
        }
      })
      .filter(_.resolvedId startsWith "NCBI:")
      .foldLeft(HashMap[String, List[Integer]]()) {
        (agg, entry) => {
          val targetTaxonIds: List[Integer] = agg.getOrElse(entry.providedName, List())
          val currentId = new Integer(entry.resolvedId.replace("NCBI:", ""))
          agg + (entry.providedName -> (currentId :: targetTaxonIds).distinct)
        }
      }

    println("taxonCache ready.")
    taxonCache
  }

  def findNames(text: String): List[String] = {
    taxonCacheNCBI.get(text) match {
      case Some(ids) => ids.map(id => s"NCBITaxon:$id")
      case None => List("no:match")
    }
  }
}

