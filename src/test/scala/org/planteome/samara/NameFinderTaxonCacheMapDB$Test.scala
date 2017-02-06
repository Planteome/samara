package org.planteome.samara

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}


class NameFinderTaxonCacheMapDB$Test extends FlatSpec with Matchers with BeforeAndAfter with NameFinderTaxonCacheMapDB {

  override def resourceNames: Seq[String] = {
    Seq("/org/planteome/samara/apsnet/taxonMap.tsv", "/org/planteome/samara/apsnet/testTaxonMap.tsv")
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
    humans should contain("NCBITaxon:9606")
    humans.size should be(1)
  }

  "name finder" should "resolve NCBI id for bean leafroll virus" in {
    val wheat = findNames("Bean leafroll virus (BLRV)")
    wheat should contain("NCBITaxon:12041")
    wheat.size should be(1)
  }


  "name finder" should "resolve to no:match for Donald Duck" in {
    val humans = findNames("Donald duck")
    humans should contain("no:match")
  }
}
