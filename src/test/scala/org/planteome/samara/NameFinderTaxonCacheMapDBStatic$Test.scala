package org.planteome.samara

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}


class NameFinderTaxonCacheMapDBStatic$Test extends FlatSpec with Matchers with BeforeAndAfter with NameFinderTaxonCacheMapDBStatic {

  "name finder" should "have access to taxon map" in {
    taxonMapStream should not(be(null))
  }

  "name finder" should "resolve NCBI id for wheat (Triticum)" in {
    val wheat = findNames("Triticum")
    wheat should not be empty
  }

  "name finder" should "resolve to no:match for Donald Duck" in {
    val humans = findNames("Donald duck")
    humans should contain("no:match")
  }
}
