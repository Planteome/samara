package org.planteome.samara

import org.scalatest.{FlatSpec, Matchers}

class TermFinderProxy$Test extends FlatSpec with Matchers with TermFinderProxy {

  "name finder" should "resolve PO id for whole plant" in {
    findTerms("whole plant") should be(List(Term("whole plant", "PO:0000003")))
  }

  "name finder" should "resolve PO id for a leaf" in {
    findTerms("leaf") should be(List(Term("leaf", "PO:0025034")))
  }

  "name finder" should "resolve PO id for a Leaf" in {
    findTerms("Leaf") should be(List(Term("leaf", "PO:0025034")))
  }

  "name finder" should "resolve PO id for a Ray" in {
    findTerms("ray") should be(List())
  }

  "name finder" should "resolve NCBI id for humans" in {
    findTerms("Homo sapiens") should be(List(Term("Homo sapiens", "NCBITaxon:9606")))
  }

}
