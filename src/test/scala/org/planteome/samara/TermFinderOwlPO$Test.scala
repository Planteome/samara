package org.planteome.samara

import org.scalatest.{FlatSpec, Matchers}

class TermFinderOwlPO$Test extends FlatSpec with Matchers with TermFinderOwlPO {

  "name finder" should "resolve PO id for whole plant" in {
    findTerms("whole plant") should be(List(Term("whole plant", "PO:0000003")))
  }

  "name finder" should "not resolve NCBI id for humans" in {
    findTerms("Homo sapiens") should not be List(Term("Homo sapiens", "NCBITaxon:9606"))
  }

  "name finder" should "not resolve ray for some reason" in {
    findTerms("Ray") should be(List())
  }

  "name finder" should "resolve part in description" in {
    findTerms("this is a whole plant") should be(List(Term("whole plant", "PO:0000003")))
  }

  "name finder" should "resolve parts in description" in {
    findTerms("this is a whole plant and a leaf") should be(List(Term("leaf", "PO:0025034"), Term("whole plant", "PO:0000003")))
  }

  "name finder" should "resolve parts in description 2" in {
    findTerms("Acrocalymma root and crown rot") should be(List(Term("root", "PO:0009005"), Term("root", "PO:0025025")))
  }

  "name finder" should "resolve parts in species" in {
    findTerms("Erysiphe cichoracearum DC") should not be List(Term("ear", "PO:0020136"))
    findTerms("Erysiphe cichoracearum DC") should be(List())
  }

}
