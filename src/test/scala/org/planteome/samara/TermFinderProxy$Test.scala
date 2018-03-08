package org.planteome.samara

import org.scalatest.{FlatSpec, Matchers}

class TermFinderProxy$Test extends FlatSpec with Matchers with TermFinderProxy {

  "name finder" should "resolve PO id for whole plant" in {
    val aWholePlantTerm = findTerms("whole plant")
    aWholePlantTerm should be(List(Term("whole plant", "PO:0000003")))
  }

  "name finder" should "resolve PO id for a leaf" in {
    val aWholePlantTerm = findTerms("leaf")
    aWholePlantTerm should be(List(Term("leaf", "PO:0025034")))
  }

  "name finder" should "resolve PO id for a Leaf" in {
    val aWholePlantTerm = findTerms("Leaf")
    aWholePlantTerm should be(List(Term("leaf", "PO:0025034")))
  }

  "name finder" should "resolve PO id for a Ray" in {
    val aWholePlantTerm = findTerms("ray")
    aWholePlantTerm should be(List())
  }

  "name finder" should "resolve NCBI id for humans" in {
    val aWholePlantTerm = findTerms("Homo sapiens")
    aWholePlantTerm should be(List(Term("Homo sapiens", "NCBITaxon:9606")))
  }

}
