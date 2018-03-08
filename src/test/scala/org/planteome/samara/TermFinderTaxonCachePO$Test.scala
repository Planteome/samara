package org.planteome.samara

import org.scalatest.{FlatSpec, Matchers}

class TermFinderTaxonCachePO$Test extends FlatSpec with Matchers with TermFinderTaxonCachePO {

  "name finder" should "resolve PO id for whole plant" in {
    val aWholePlantTerm = findTerms("whole plant")
    aWholePlantTerm should be(List(Term("whole plant", "PO:0000003")))
  }

}
