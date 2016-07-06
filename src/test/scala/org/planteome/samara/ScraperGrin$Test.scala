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
    val accessionIds: Iterable[(Int, Descriptor)] = ScraperGrin.getAccessionIds(cropIds)
    println(s"counted [${accessionIds.size}] accessors")
    println(s"counted [${accessionIds.toSeq.distinct.size}] distinct accessors")
    accessionIds.size should be > 0
  }


  "scraper" should "be able to retrieve info for individual accessions" in {
    val accessionId = 1265054
    val objs: Iterable[Observation] = ScraperGrin.getObservationsForAccession(accessionId)
    val expectedDescriptor: Descriptor = Descriptor(id = 65098,
      definition = Some("Reaction to Stripe Rust (incited by Puccinia striiformis) in the adult plant stage in the field.  See also related descriptor Stripe Rust Adult Severity.  Grown in Mt. Vernon."),
      name = None)
    val expectedMethod: Method = Method(402008, Some("WHEAT.STRIPERUST.MTVERNON.87"))
    val expectedTaxon: Taxon = Taxon(name = "Triticum monococcum L. subsp. monococcum", id = 40597)
    val expectedAccession: Accession = Accession(id = accessionId, name = "", number = "PI 355548")

    val obs: Observation = Observation(taxon = expectedTaxon,
      descriptor = expectedDescriptor,
      method = expectedMethod,
      value = "0 - RESISTANT, NO SYMPTOMS",
      accession = expectedAccession)

    objs.size should be > 0
    objs should contain(obs)
    objs.foreach(println)
  }


}