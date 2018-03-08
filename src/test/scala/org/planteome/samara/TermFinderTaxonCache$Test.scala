package org.planteome.samara

import org.scalatest.{FlatSpec, Matchers}



class TermFinderTaxonCache$Test extends FlatSpec with Matchers with TermFinderTaxonCache {
  "name finder" should "have access to taxon map" in {
    taxonMapStream should not(be(null))
  }

  "name finder" should "resolve NCBI id for wheat (Triticum)" in {
    val wheat = findTerms("Triticum")
    wheat should not be empty
  }

  "name finder" should "resolve NCBI id for Homo sapiens" in {
    val humans = findTerms("Homo sapiens")
    humans should contain(Term("Homo sapiens", "NCBITaxon:9606"))
  }

  "name finder" should "resolve NCBI id for Glycine max" in {
    val humans = findTerms("Glycine max")
    humans should contain(Term("Glycine max", "NCBITaxon:3847"))
  }

  "name finder" should "resolve NCBI id for Glycine max 2" in {
    val humans = findTerms("Glycine max [L.] Merr.")
    humans should contain(Term("Glycine max [L.] Merr.", "NCBITaxon:3847"))
  }

  "name finder" should "resolve NCBI id for Sorghum bicolor" in {
    val humans = findTerms("Diseases of Sorghum (Sorghum bicolor (L.) Moench)")
    humans should contain(Term("Glycine max [L.] Merr.", "NCBITaxon:3847"))
  }

  "name finder" should "resolve to no:match for Donald Duck" in {
    val humans = findTerms("Donald duck")
    humans should be(List())
  }

  "name finder" should "resolve to some virus" in {
    val humans = findTerms("Barley yellow striate mosaic virus")
    humans should be(List(Term("Barley yellow striate mosaic virus", "NCBITaxon:1985699")))
  }
}
