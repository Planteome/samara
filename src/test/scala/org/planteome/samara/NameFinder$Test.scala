package org.planteome.samara

import org.scalatest.{Matchers, FlatSpec}



class NameFinder$Test extends FlatSpec with Matchers  {

  "name finder" should "have access to taxon map" in {
    val taxonMap = Samara.getClass.getResource("/org/eol/globi/taxon/taxonMap.tsv.gz")
    taxonMap should not(be(null))
  }

}

