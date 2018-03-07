package org.planteome.samara

trait NameFinderOwlPO extends NameFinderTaxonCache {
  override def taxonMapCacheConfig: TaxonMapCache = {
    val prefix = "PO:"
    val lineFilter: (String) => Boolean = { (s: String) => s.contains(prefix) }
    val prefixFilter: (TaxonMap) => Boolean = { (map: TaxonMap) => map.resolvedId startsWith prefix }
    val prefixMap: (TaxonMap) => (String, List[Integer]) = { (entry: TaxonMap) => (entry.providedName, List(new Integer(entry.resolvedId.replace("PO:", "").trim))) }
    val expandId: (Integer) => String = { (id: Integer) => s"$prefix${"%07d".format(id)}" }
    TaxonMapCache(name = "OwlPO", lineFilter = lineFilter, prefixFilter = prefixFilter, prefixMap = prefixMap, expandId = expandId)
  }

  override def reducedTaxonMap: Iterator[(String, List[Integer])] = {
    val prefix = "http://purl.obolibrary.org/obo/PO_"
    OwlPOLoader
      .plantOntology
      .filter(t => {
        t._1.startsWith(prefix)
      }).map(t => {
      (t._1.replace("http://purl.obolibrary.org/obo/PO_", ""), t._2)
    })
      .map(t => (t._2, List(new Integer(t._1))))
  }

}
