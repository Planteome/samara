package org.planteome.samara

trait TermFinderOwlPO extends TermFinder {
  def taxonMapCacheConfig: TaxonMapCache = {
    val prefix = "PO:"
    val lineFilter: (String) => Boolean = { (s: String) => s.contains(prefix) }
    val prefixFilter: (TaxonMap) => Boolean = { (map: TaxonMap) => map.resolvedId startsWith prefix }
    val prefixMap: (TaxonMap) => (String, List[Integer]) = { (entry: TaxonMap) => (entry.providedName, List(new Integer(entry.resolvedId.replace("PO:", "").trim))) }
    val expandId: (Integer) => String = { (id: Integer) => s"$prefix${"%07d".format(id)}" }
    TaxonMapCache(name = "OwlPO", lineFilter = lineFilter, prefixFilter = prefixFilter, prefixMap = prefixMap, expandId = expandId)
  }

  def labelIdPairs: List[(String, Integer)] = {
    Console.err.println(s"taxonCache for [${taxonMapCacheConfig.name}] building...")
    val start = System.currentTimeMillis()

    val prefix = "http://purl.obolibrary.org/obo/PO_"
    val pairs = OwlPOLoader
      .plantOntology
      .filter(t => {
        t._1.startsWith(prefix) && !t._2.equalsIgnoreCase("ray")
      }).map(t => {
      (t._1.replace("http://purl.obolibrary.org/obo/PO_", ""), t._2)
    })
      .map(t => (t._2, new Integer(t._1))).toList
    val end = System.currentTimeMillis()
    Console.err.println(s"taxonCache for [${taxonMapCacheConfig.name}] ready [took ${(end - start) / 1000} s].")
    pairs
  }
  lazy val labelId = labelIdPairs

  def findTerms(text: String): List[Term] = {
    labelId
      .filter(p => text.toLowerCase.contains(p._1))
      .map(p => Term(p._1, taxonMapCacheConfig.expandId(p._2)))
  }

}
