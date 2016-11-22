package org.planteome.samara

import org.scalatest.{FlatSpec, Matchers}


class IdFinderTaxonCache$Test extends FlatSpec with Matchers with IdFinderTaxonCache {

  "id finder" should "have access to taxon map" in {
    taxonMapStream should not(be(null))
  }

  "id finder" should "resolve NCBI id Medicago sativa" in {
    val idsForNCBITaxon = findIds("NCBITaxon:3879")
    idsForNCBITaxon should contain("GRINTaxon:300359")
  }

  "id finder" should "resolve GRIN id for Medicago sativa" in {
    val idsForGRINMedicagoSativa = findIds("GRINTaxon:300359")
    idsForGRINMedicagoSativa should contain("NCBITaxon:3879")
  }

}
