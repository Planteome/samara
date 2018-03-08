package org.planteome.samara

object NCBI extends TermFinderTaxonCacheMapDB
object PO extends TermFinderTaxonCachePO with TermFinderTaxonCacheMapDB
object OwlPO extends TermFinderOwlPO

trait TermFinderProxy extends TermFinder {

  def findTerms(text: String): List[Term] = {
    val finders = List[TermFinder](NCBI, PO, OwlPO)

    finders
      .find(_.findTerms(text).nonEmpty)
      .map(_.findTerms(text))
      .getOrElse(List())
  }
}

