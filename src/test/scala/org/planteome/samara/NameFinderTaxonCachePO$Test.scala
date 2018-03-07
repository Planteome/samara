package org.planteome.samara

import org.scalatest.{FlatSpec, Matchers}

class NameFinderTaxonCachePO$Test extends FlatSpec with Matchers with NameFinderTaxonCachePO {

  "name finder" should "resolve PO id for whole plant" in {
    val aWholePlantTerm = findNames("whole plant")
    aWholePlantTerm should be(List("PO:0000003"))
  }

}
