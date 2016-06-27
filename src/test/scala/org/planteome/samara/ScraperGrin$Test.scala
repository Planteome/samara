package org.planteome.samara

import org.scalatest._


class ScraperGrin$Test extends FlatSpec with Matchers {

  val apple = Crop(115)
  val wheat = Crop(65)

  "scraper" should "enable retrieval of crop ids" in {
    val cropIds: Iterable[Crop] = ScraperGrin.getCropIds()
    cropIds should contain(apple)
    cropIds should contain(wheat)
  }


  "scraper" should "be able to retrieve accessors for apple crops" in {
    val cropIds: Seq[Crop] = Seq(apple)
    val accessionIds: Iterable[Int] = ScraperGrin.getAccessionIds(cropIds)
    println(s"counted [${accessionIds.size}] accessors")
    println(s"counted [${accessionIds.toSeq.distinct.size}] distinct accessors")
    accessionIds.size should be > 0
  }


  "scraper" should "be able to retrieve info for individual accessions" in {
    val firstAccessionId = 1265054
    val objs: Iterable[Observation] = ScraperGrin.getObservationsForAccession(firstAccessionId)
    val obs: Observation = Observation("Triticum monococcum L. subsp. monococcum", List(Taxon("genus", "Triticum", 12442), Taxon("family", "Poaceae", 897), Taxon("subfamily", "Pooideae", 1472), Taxon("tribe", "Triticeae", 1317)), Descriptor(65098), Method(402008, Descriptor(65098)), "0 - RESISTANT, NO SYMPTOMS", 1265054)
    objs.size should be > 0
    objs should contain(obs)
    objs.foreach(println)
  }


}