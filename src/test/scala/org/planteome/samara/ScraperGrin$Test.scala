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
    val accessionId = 1265054
    val objs: Iterable[Observation] = ScraperGrin.getObservationsForAccession(accessionId)
    val expectedDescriptorDefinition = "Reaction to Stripe Rust (incited by Puccinia striiformis) in the adult plant stage in the field.  See also related descriptor Stripe Rust Adult Severity.  Grown in Mt. Vernon."
    val expectedMethodName = "WHEAT.STRIPERUST.MTVERNON.87"

    val obs: Observation = Observation(taxon = Taxon(name = "Triticum monococcum L. subsp. monococcum", id = 40597),
      descriptor = Descriptor(65098,Some(expectedDescriptorDefinition)),
      method = Method(402008, Some(expectedMethodName)),
      value = "0 - RESISTANT, NO SYMPTOMS",
      accessionId = accessionId)

    objs.size should be > 0
    objs should contain(obs)
    objs.foreach(println)
  }


}