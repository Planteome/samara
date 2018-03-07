package org.planteome.samara

import org.scalatest.{FlatSpec, Matchers}

class NameFinderProxy$Test extends FlatSpec with Matchers with NameFinderProxy {

  "name finder" should "resolve PO id for whole plant" in {
    val aWholePlantTerm = findNames("whole plant")
    aWholePlantTerm should be(List("PO:0000003"))
  }

  "name finder" should "resolve NCBI id for humans" in {
    val aWholePlantTerm = findNames("Homo sapiens")
    aWholePlantTerm should be(List("NCBITaxon:9606"))
  }

}
