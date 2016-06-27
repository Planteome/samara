package org.planteome.samara

import org.scalatest._


class ScraperApsnet$Test extends FlatSpec with Matchers  {

  "scraper" should "produce a giant list of disease interactions" in {
    val diseases = ScraperApsnet.scrapeDiseases()

    diseases should contain(Disease("Bacterial canker", "Pseudomonas syringae pv. syringae van Hall 1902", "Diseases of Peach and Nectarine Peach: Prunus persica (L.) Batsch Nectarine: P. persica var. nucipersica (Suckow) C.K. Schneid."))
    diseases should contain(Disease("Alfalfa witches’-broom", "‘Candidatus Phytoplasma asteris’", "Diseases of Alfalfa (Medicago sativa L.)"))

    println(s"found ${diseases.size} disease interactions:")

  }

}