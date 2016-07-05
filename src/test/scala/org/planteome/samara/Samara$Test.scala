package org.planteome.samara

import org.scalatest._


class Samara$Test extends FlatSpec with Matchers  {

  "scraper" should "produce a giant list of disease interactions" in {
    Samara.main(Array.empty[String])

  }

}