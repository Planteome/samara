package org.planteome.samara

import org.scalatest.{FlatSpec, Matchers}

class NameFinderOwlPO$Test extends FlatSpec with Matchers with NameFinderOwlPO {

  "name finder" should "resolve PO id for whole plant" in {
    val aWholePlantTerm = findNames("whole plant")
    aWholePlantTerm should be(List("PO:0000003"))
  }

  "name finder" should "not resolve NCBI id for humans" in {
    val aWholePlantTerm = findNames("Homo sapiens")
    aWholePlantTerm should not be List("NCBITaxon:9606")
  }

}
