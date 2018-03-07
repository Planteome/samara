package org.planteome.samara

import com.hp.hpl.jena.ontology.OntModel
import com.hp.hpl.jena.query.{QueryExecutionFactory, QueryFactory}
import com.hp.hpl.jena.rdf.model.ModelFactory
import scala.collection.JavaConverters._

object OwlPOLoader {

  def plantOntology: Iterator[(String, String)] = {
    val m = ModelFactory.createOntologyModel()
    //load onto file (assuming that is has been installed through sbt
    val resource = this.getClass.getResource("/org/planteome/plant-ontology/plant-ontology-master/po.owl")
    if (resource == null) {
      throw new RuntimeException("please load plant-ontology using sbt")
    }
    m.read(resource.toURI.toString)

    //Get all po + names + synonyms
    val queryString =
      """SELECT DISTINCT ?s ?label
        | WHERE {  {?s <http://www.w3.org/2000/01/rdf-schema#label> ?label . ?s  <http://www.w3.org/2000/01/rdf-schema#subClassOf>* <http://purl.obolibrary.org/obo/PO_0009011>}
        | UNION {?s <http://www.geneontology.org/formats/oboInOwl#hasRelatedSynonym> ?label . }
        | UNION {?s <http://www.geneontology.org/formats/oboInOwl#hasBroadSynonym> ?label . }
        | UNION {?s <http://www.geneontology.org/formats/oboInOwl#hasNarrowSynonym> ?label . } }""".stripMargin
    extractTerms(m, queryString)
  }

  def extractTerms(m: OntModel, queryString: String): Iterator[(String, String)] = {
    val query = QueryFactory.create(queryString)
    val qexec = QueryExecutionFactory.create(query, m)
    try {
      val rs = qexec.execSelect()
      rs.asScala.map(rb => {
        val uri = rb.get("s")
        val resource = uri.asResource.getURI
        val x = rb.get("label")
        var label = x.asNode.getLiteralLexicalForm
        label = label.split(" \\(")(0)
        (resource, label)
      }).toList.iterator
    } finally {
      qexec.close()
    }
  }
}
