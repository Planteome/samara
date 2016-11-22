package org.planteome.samara

object ScraperNCBILinkOut extends Scraper with ResourceUtil with NCBILinkOut {

  object Parser extends ParserGrin

  override def scrape() = {
    println("lo_taxon_id\ttaxon_id")
    val pairs: Seq[(Int, Int)] = collectLinkOutPairsFor(findNCBITaxonIdsWithGRINTaxonLinkOut)
    pairs
      .map(tuple => s"GRINTaxon:${tuple._1.toString}\tNCBITaxon:${tuple._2.toString}")
      .foreach(println)
  }

}
