package org.planteome.samara

object NCBI extends NameFinderTaxonCacheMapDB
object PO extends NameFinderTaxonCachePO with NameFinderTaxonCacheMapDB
object OwlPO extends NameFinderOwlPO with NameFinderTaxonCacheMapDB

trait NameFinderProxy extends NameFinder {

  def findNames(text: String): List[String] = {
    val finders = List[NameFinder](NCBI, PO, OwlPO)

    finders
      .find(finder => !finder.findNames(text).contains("no:match"))
      .map(finder => finder.findNames(text))
      .getOrElse(List("no:match"))
  }
}

