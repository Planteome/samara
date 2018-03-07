package org.planteome.samara

object NCBI extends NameFinderTaxonCacheMapDB
object PO extends NameFinderTaxonCachePO with NameFinderTaxonCacheMapDB

trait NameFinderProxy extends NameFinder {

  def findNames(text: String): List[String] = {
    val names = NCBI.findNames(text)
    if (names.contains("no:match")) {
      PO.findNames(text)
    } else {
      names
    }

  }
}

