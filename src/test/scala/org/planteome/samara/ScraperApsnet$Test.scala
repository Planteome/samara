package org.planteome.samara

import org.scalatest._


class ScraperApsnet$Test extends FlatSpec with Matchers {

  "scraper" should "produce a giant list of disease interactions" in {
    val diseases = ScraperApsnet.scrapeDiseases().map(_._3)

    diseases should contain(Disease(name = "Bacterial canker",
      verbatimPathogen = "Pseudomonas syringae pv. syringae van Hall 1902",
      pathogen = "Pseudomonas syringae pv. syringae van Hall 1902",
      verbatimHost = expectedHosts.verbatimPeachHost,
      host = "Prunus persica",
      citation = expectedCitations.peachAndNectarine))

    diseases should contain(Disease(name = "Bacterial canker",
      verbatimPathogen = "Pseudomonas syringae pv. syringae van Hall 1902",
      pathogen = "Pseudomonas syringae pv. syringae van Hall 1902",
      verbatimHost = expectedHosts.verbatimPeachHost,
      host = "Prunus persica var. nucipersica",
      citation = expectedCitations.peachAndNectarine))

    diseases should contain(Disease(name = "Green fruit rot",
      verbatimPathogen = "M. laxa (Aderhold & Ruhland) Honey",
      pathogen = "Monilinia laxa (Aderhold & Ruhland) Honey",
      verbatimHost = expectedHosts.verbatimPeachHost,
      host = "Prunus persica",
      citation = expectedCitations.peachAndNectarine))

    diseases should contain(Disease(name = "Alfalfa witches’-broom",
      verbatimPathogen = "‘Candidatus Phytoplasma asteris’",
      pathogen = "Candidatus Phytoplasma asteris",
      verbatimHost = "Diseases of Alfalfa (Medicago sativa L.)",
      host = "Medicago sativa L.",
      citation = expectedCitations.alfalfa))


    println(s"found ${diseases.size} disease interactions:")

  }

}