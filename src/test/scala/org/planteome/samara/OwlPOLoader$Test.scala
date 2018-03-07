package org.planteome.samara

import org.scalatest.{FlatSpec, Matchers}


class OwlPOLoader$Test extends FlatSpec with Matchers {

  "plant ontology" should "load" in {
    val ontology = OwlPOLoader.plantOntology
    val wholePlant = ontology.filter(p => p._2 == "whole plant")
    wholePlant.toList should contain ("http://purl.obolibrary.org/obo/PO_0000003", "whole plant")
  }

}
