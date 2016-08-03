package org.planteome.samara

import net.ruippeixotog.scalascraper.model.Document
import org.scalatest._


object ParserApsnetStatic$ extends ParserApsnet with NameFinderStatic

object expectedCitations {
  val alfalfa = "D. A. Samac, L. H. Rhodes, and W. O. Lamp, primary collators (Last update: 6/25/14). Diseases of Alfalfa (Medicago sativa L.). The American Phytopathological Society."
  val peachAndNectarine = "J. K. Uyemoto, J. M. Ogawa, and B. A. Jaffee, primary collators; updated by J. E. Adaskaveg, S. W. Scott, and H. Scherm (last update 5/23/01). Diseases of Peach and Nectarine Peach: Prunus persica (L.) Batsch Nectarine: P. persica var. nucipersica (Suckow) C.K. Schneid.. The American Phytopathological Society."

}

class ParserApsnet$Test extends FlatSpec with Matchers with NameFinderStatic with ParseTestUtil {
  "Parsing Alfalfa" should "result in pathogen-host interactions for disease" in {
    val doc: Document = parse("apsnet/Alfalfa.aspx")
    val interactions: Iterable[Disease] = ParserApsnetStatic$.parse(doc)

    interactions should contain(Disease(name = "Alfalfa witches’-broom", pathogen = "‘Candidatus Phytoplasma asteris’", host = "Medicago sativa L.", citation = expectedCitations.alfalfa))
    interactions should contain(Disease("Red clover vein mosaic", "Red clover vein mosaic virus (RCVMV)", "Medicago sativa L.", citation = expectedCitations.alfalfa))
  }

  "Parsing Peach and Nectarine" should "result in pathogen-host interactions for disease" in {
    val doc: Document = parse("apsnet/PeachandNectarine.aspx")
    val interactions: Iterable[Disease] = ParserApsnetStatic$.parse(doc)

    interactions should contain(Disease(name = "Bacterial canker", pathogen = "Pseudomonas syringae pv. syringae van Hall 1902", host = "Prunus persica", citation = expectedCitations.peachAndNectarine))
    interactions should contain(Disease(name = "Bacterial canker", pathogen = "Pseudomonas syringae pv. syringae van Hall 1902", host = "Prunus persica var. nucipersica", citation = expectedCitations.peachAndNectarine))
    interactions should contain(Disease(name = "Bacterial canker", pathogen = "Pseudomonas syringae pv. syringae van Hall 1902", host = "Prunus persica", citation = expectedCitations.peachAndNectarine))
    interactions should contain(Disease(name = "Bacterial canker", pathogen = "Pseudomonas syringae pv. syringae van Hall 1902", host = "Prunus persica var. nucipersica", citation = expectedCitations.peachAndNectarine))

    interactions should contain(Disease(name = "Green fruit rot", pathogen = "Monilinia fructicola (G. Wint.) Honey", host = "Prunus persica", citation = expectedCitations.peachAndNectarine))
    interactions should contain(Disease(name = "Green fruit rot", pathogen = "Monilinia fructicola (G. Wint.) Honey", host = "Prunus persica var. nucipersica", citation = expectedCitations.peachAndNectarine))
    interactions should contain(Disease(name = "Green fruit rot", pathogen = "Monilinia laxa (Aderhold & Ruhland) Honey", host = "Prunus persica", citation = expectedCitations.peachAndNectarine))
    interactions should contain(Disease(name = "Green fruit rot", pathogen = "Monilinia laxa (Aderhold & Ruhland) Honey", host = "Prunus persica var. nucipersica", citation = expectedCitations.peachAndNectarine))
  }

  "scrubbing name" should "remove whitespaces" in {
    val stringWithWeirdwhitespaces: String = "Mycoleptodiscus terrestris (Gerd.) Ostaz.           (syn. Leptodiscus terrestris Gerd.)"
    ParserApsnetStatic$.scrub(stringWithWeirdwhitespaces) should be("Mycoleptodiscus terrestris (Gerd.) Ostaz. (syn. Leptodiscus terrestris Gerd.)")
  }

  "Parsing disease index" should "result in a list of urls to species pages" in {
    val doc: Document = parse("apsnet/default.aspx")
    val links = ParserApsnetStatic$.parsePageIndex(doc)

    links should contain("http://www.apsnet.org/publications/commonnames/Pages/AfricanDaisy.aspx")
    links should contain("http://www.apsnet.org/publications/commonnames/Pages/PeachandNectarine.aspx")
  }

  "extract names" should "produce a list of extracted hostnames" in {
    val hostNames: String = "Diseases of Peach and Nectarine Peach: Prunus persica (L.) Batsch Nectarine: P. persica var. nucipersica (Suckow) C.K. Schneid."
    ParserApsnetStatic$.extractHostNames(hostNames) should contain("Prunus persica")
    ParserApsnetStatic$.extractHostNames(hostNames) should contain("Prunus persica var. nucipersica")
  }

  "expand prefixes" should "fill in genus names of previously mentioned species" in {
    ParserApsnetStatic$.expandPrefixes(List("Homo sapiens", "H. sapiens", "Ariopsis felis")) should be(List("Homo sapiens", "Homo sapiens", "Ariopsis felis"))
  }

}