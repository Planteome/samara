package org.planteome.samara

trait TermFinderTaxonCachePO extends TermFinderTaxonCache {
  override def taxonMapCacheConfig: TaxonMapCache = {
    val prefix = "PO:"
    val lineFilter: (String) => Boolean = { (s: String) => s.contains(prefix) }
    val prefixFilter: (TaxonMap) => Boolean = { (map: TaxonMap) => map.resolvedId startsWith prefix }
    val prefixMap: (TaxonMap) => (String, List[Integer]) = { (entry: TaxonMap) => (entry.providedName, List(new Integer(entry.resolvedId.replace("PO:", "").trim))) }
    val expandId: (Integer) => String = { (id: Integer) => s"$prefix${"%07d".format(id)}" }

    TaxonMapCache(name = "PO"
      , lineFilter = lineFilter
      , prefixFilter = prefixFilter
      , prefixMap = prefixMap
      , expandId = expandId)
  }
}
