package org.planteome.samara

import java.io.InputStream
import java.util.zip.GZIPInputStream

import org.scalatest.{Matchers, FlatSpec}

import scala.collection.immutable.HashMap
import scala.io.Source


case class TaxonMap(providedId: String, providedName: String, resolvedId: String, resolvedName: String)

class NameFinder$Test extends FlatSpec with Matchers {

  "name finder" should "have access to taxon map" in {
    taxonMapStream should not(be(null))
  }

  def taxonMapStream: InputStream = {
    new GZIPInputStream(Samara.getClass.getResourceAsStream("/org/eol/globi/taxon/taxonMap.tsv.gz"))
  }

  "name finder" should "resolve NCBI id for wheat (Triticum)" in {
    val ncbiTaxa: Map[String, List[String]] = Source.fromInputStream(taxonMapStream)
      .getLines()
      .flatMap(line => {
        val parts = line.split("\t").toList
        if (parts.size > 3) {
          Some(TaxonMap(parts(0), parts(1), parts(2), parts(3)))
        } else {
          None
        }
      })
      .filter(_.resolvedId startsWith "NCBI:")
      .foldLeft(new HashMap[String, List[String]].empty) {
        (agg, entry) => {
          val targetTaxonIds = agg.getOrElse(entry.providedName, List())
          agg + (entry.providedName -> (entry.resolvedId :: targetTaxonIds).distinct)
        }
      }

    val wheat = ncbiTaxa.keys.filter(_.startsWith("Triticum"))
    wheat should not be empty
    ncbiTaxa.keys.size should not be(0)
  }
}
