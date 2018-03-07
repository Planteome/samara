package org.planteome.samara

import org.scalatest._


class ScraperApsnet$Test extends FlatSpec with Matchers {

  "scraper" should "produce a giant list of disease interactions" in {
    val diseases = ScraperApsnet.scrapeDiseases().map(_._3)
    diseases should not be empty

    println(s"found ${diseases.size} disease interactions:")
  }

}