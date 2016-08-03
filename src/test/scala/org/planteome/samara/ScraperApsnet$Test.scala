package org.planteome.samara

import org.scalatest._


class ScraperApsnet$Test extends FlatSpec with Matchers  {

  "scraper" should "produce a giant list of disease interactions" in {
    val diseases = ScraperApsnet.scrapeDiseases()

    diseases should contain(Disease("Bacterial canker", "Pseudomonas syringae pv. syringae van Hall 1902", "Prunus persica"))
    diseases should contain(Disease("Bacterial canker", "Pseudomonas syringae pv. syringae van Hall 1902", "Prunus persica var. nucipersica"))
    diseases should contain(Disease("Alfalfa witches’-broom", "‘Candidatus Phytoplasma asteris’", "Medicago sativa L."))
    diseases should contain(Disease("Green fruit rot", "Monilinia laxa (Aderhold & Ruhland) Honey", "Prunus persica"))

    println(s"found ${diseases.size} disease interactions:")

  }

}