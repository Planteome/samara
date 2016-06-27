package org.planteome.samara

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Document
import org.scalatest._


object ParserApsnetStatic$ extends ParserApsnet with NameFinderStatic {
  def parsePageIndex(doc: Document) : Iterable[String] = {
    doc >> elements(".link-item") >> attrs("href")("a")
  }

}

class ParserApsnet$Test extends FlatSpec with Matchers with NameFinderStatic with ParseTestUtil {

  "Parsing Alfalfa" should "result in pathogen-host interactions for disease" in {
    val doc: Document = parse("apsnet/Alfalfa.aspx")
    val interactions: Iterable[Disease] = ParserApsnetStatic$.parse(doc)

    interactions should contain(Disease("Alfalfa witches’-broom", "‘Candidatus Phytoplasma asteris’", "Diseases of Alfalfa (Medicago sativa L.)"))
    interactions should contain(Disease("Red clover vein mosaic", "Red clover vein mosaic virus (RCVMV)", "Diseases of Alfalfa (Medicago sativa L.)"))
  }

  "Parsing Peach and Nectarine" should "result in pathogen-host interactions for disease" in {
    val doc: Document = parse("apsnet/PeachandNectarine.aspx")
    val interactions: Iterable[Disease] = ParserApsnetStatic$.parse(doc)

    interactions should contain(Disease("Bacterial canker", "Pseudomonas syringae pv. syringae van Hall 1902", "Diseases of Peach and Nectarine Peach: Prunus persica (L.) Batsch Nectarine: P. persica var. nucipersica (Suckow) C.K. Schneid."))
    interactions should contain(Disease("Bacterial canker", "Pseudomonas syringae pv. syringae van Hall 1902", "Diseases of Peach and Nectarine Peach: Prunus persica (L.) Batsch Nectarine: P. persica var. nucipersica (Suckow) C.K. Schneid."))

    interactions should contain(Disease("Green fruit rot", "Monilinia fructicola (G. Wint.) Honey", "Diseases of Peach and Nectarine Peach: Prunus persica (L.) Batsch Nectarine: P. persica var. nucipersica (Suckow) C.K. Schneid."))
    interactions should contain(Disease("Green fruit rot", "M. laxa (Aderhold & Ruhland) Honey", "Diseases of Peach and Nectarine Peach: Prunus persica (L.) Batsch Nectarine: P. persica var. nucipersica (Suckow) C.K. Schneid."))
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
    val doc: Document = get("http://www.apsnet.org/publications/commonnames/Pages/default.aspx")
    val pages = ParserApsnetStatic$.parsePageIndex(doc)
    val diseases = pages.flatMap(page => {
      val pageDoc = get(page)
      val results = ParserApsnetStatic$.parse(pageDoc)
      results
    })

    diseases should contain(Disease("Bacterial canker", "Pseudomonas syringae pv. syringae van Hall 1902", "Diseases of Peach and Nectarine Peach: Prunus persica (L.) Batsch Nectarine: P. persica var. nucipersica (Suckow) C.K. Schneid."))
    diseases should contain(Disease("Alfalfa witches’-broom", "‘Candidatus Phytoplasma asteris’", "Diseases of Alfalfa (Medicago sativa L.)"))

    println(s"found ${diseases.size} disease interactions:")
    println("disease\tpathogen\thost")
    diseases.foreach(disease => {
      println(s"${disease.name}\t${disease.pathogen}\t${disease.host}")
    })
  }

}