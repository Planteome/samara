package org.planteome.samara

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}


class TermFinderTaxonCacheMapDB$Test extends FlatSpec with Matchers with BeforeAndAfter with TermFinderTaxonCacheMapDB {

  override def resourceNames: Seq[String] = {
    Seq("/org/planteome/samara/apsnet/taxonMap.tsv", "/org/planteome/samara/apsnet/testTaxonMap.tsv")
  }

  "name finder" should "have access to taxon map" in {
    taxonMapStream should not(be(null))
  }

  "name finder" should "resolve NCBI id for Homo sapiens" in {
    val humans = findTerms("Homo sapiens")
    humans should be(List(Term("Homo sapiens", "NCBITaxon:9606")))
  }

  "name finder" should "resolve NCBI id for bean leafroll virus" in {
    val wheat = findTerms("Bean leafroll virus (BLRV)")
    wheat should contain(Term("Bean leafroll virus (BLRV)", "NCBITaxon:12041"))
    wheat.size should be(1)
  }

  "name finder" should "resolve to no:match for Donald Duck" in {
    val humans = findTerms("Donald duck")
    humans should be(List())
  }
}
