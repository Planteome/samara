package org.planteome.samara

import org.scalatest.{FlatSpec, Matchers}



class NameFinderTaxonCache$Test extends FlatSpec with Matchers with NameFinderTaxonCache {
  "name finder" should "have access to taxon map" in {
    taxonMapStream should not(be(null))
  }

  "name finder" should "resolve NCBI id for wheat (Triticum)" in {
    val wheat = findNames("Triticum")
    wheat should not be empty
    wheat should not be List("no:match")
  }

  "name finder" should "resolve NCBI id for Homo sapiens" in {
    val humans = findNames("Homo sapiens")
    humans should contain("NCBITaxon:9606")
  }

  "name finder" should "resolve to no:match for Donald Duck" in {
    val humans = findNames("Donald duck")
    humans should contain("no:match")
  }

  "name finder" should "resolve to some virus" in {
    val humans = findNames("Barley yellow striate mosaic virus")
    humans should be(List("NCBITaxon:1985699"))
  }
}
