package org.planteome.samara

object ScraperNCBILinkOut extends Scraper with ResourceUtil with NCBILinkOut {

  object Parser extends ParserGrin

  override def scrape() = {
    println("from_taxon_id\tto_taxon_id")
    val pairs: Seq[(Int, Int)] = collectLinkOutPairsFor(findNCBITaxonIdsWithGRINTaxonLinkOut)
    pairs
      .map(tuple => s"NCBITaxon:${tuple._2.toString}\tGRINTaxon:${tuple._1.toString}")
      .foreach(println)
  }

}
