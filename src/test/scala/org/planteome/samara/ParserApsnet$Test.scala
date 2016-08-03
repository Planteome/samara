package org.planteome.samara

import net.ruippeixotog.scalascraper.model.Document
import org.scalatest._


object ParserApsnetStatic$ extends ParserApsnet with NameFinderStatic

class ParserApsnet$Test extends FlatSpec with Matchers with NameFinderStatic with ParseTestUtil {

  "Parsing Alfalfa" should "result in pathogen-host interactions for disease" in {
    val doc: Document = parse("apsnet/Alfalfa.aspx")
    val interactions: Iterable[Disease] = ParserApsnetStatic$.parse(doc)

    interactions should contain(Disease("Alfalfa witches’-broom", "‘Candidatus Phytoplasma asteris’", "Medicago sativa L."))
    interactions should contain(Disease("Red clover vein mosaic", "Red clover vein mosaic virus (RCVMV)", "Medicago sativa L."))
  }

  "Parsing Peach and Nectarine" should "result in pathogen-host interactions for disease" in {
    val doc: Document = parse("apsnet/PeachandNectarine.aspx")
    val interactions: Iterable[Disease] = ParserApsnetStatic$.parse(doc)

    interactions should contain(Disease("Bacterial canker", "Pseudomonas syringae pv. syringae van Hall 1902", "Prunus persica"))
    interactions should contain(Disease("Bacterial canker", "Pseudomonas syringae pv. syringae van Hall 1902", "Prunus persica var. nucipersica"))
    interactions should contain(Disease("Bacterial canker", "Pseudomonas syringae pv. syringae van Hall 1902", "Prunus persica"))
    interactions should contain(Disease("Bacterial canker", "Pseudomonas syringae pv. syringae van Hall 1902", "Prunus persica var. nucipersica"))

    interactions should contain(Disease("Green fruit rot", "Monilinia fructicola (G. Wint.) Honey", "Prunus persica"))
    interactions should contain(Disease("Green fruit rot", "Monilinia fructicola (G. Wint.) Honey", "Prunus persica var. nucipersica"))
    interactions should contain(Disease("Green fruit rot", "Monilinia laxa (Aderhold & Ruhland) Honey", "Prunus persica"))
    interactions should contain(Disease("Green fruit rot", "Monilinia laxa (Aderhold & Ruhland) Honey", "Prunus persica var. nucipersica"))
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

  "get all pages" should "produce a giant list of disease interactions" in {
    val diseases = ScraperApsnet.scrapeDiseases()

    diseases should contain(Disease("Bacterial canker", "Pseudomonas syringae pv. syringae van Hall 1902", "Prunus persica"))
    diseases should contain(Disease("Bacterial canker", "Pseudomonas syringae pv. syringae van Hall 1902", "Prunus persica var. nucipersica"))
    diseases should contain(Disease("Alfalfa witches’-broom", "‘Candidatus Phytoplasma asteris’", "Medicago sativa L."))

    println(s"found ${diseases.size} disease interactions:")

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