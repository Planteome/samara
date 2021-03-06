package org.planteome.samara

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}


class TermFinderTaxonCacheMapDBStatic$Test extends FlatSpec with Matchers with BeforeAndAfter with TermFinderTaxonCacheMapDBStatic {

  "name finder" should "have access to taxon map" in {
    taxonMapStream should not(be(null))
  }

  "name finder" should "resolve to no:match for Donald Duck" in {
    val humans = findTerms("Donald duck")
    humans should be(List())
  }
}
