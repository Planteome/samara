package org.planteome.samara

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

import scala.io.Source


class NameFinderTaxonCacheMapDB$Test extends FlatSpec with Matchers with BeforeAndAfter with NameFinderTaxonCacheMapDB {

  override def taxonMapLines: Iterator[String] = {
    Source.fromInputStream(taxonMapStream)
      .getLines()
  }


  "name finder" should "have access to taxon map" in {
    taxonMapStream should not(be(null))
  }

  "name finder" should "resolve NCBI id for wheat (Triticum)" in {
    val wheat = findNames("Triticum")
    wheat should not be empty
  }

  "name finder" should "resolve NCBI id for Homo sapiens" in {
    val humans = findNames("Homo sapiens")
    humans should contain("NCBITaxon:9606").or(contain("NCBITaxon:741158"))
  }

  "name finder" should "resolve to no:match for Donald Duck" in {
    val humans = findNames("Donald duck")
    humans should contain("no:match")
  }
}
