package org.planteome.samara

import java.io.File

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.model.Document
import org.scalatest._


trait NameFinderStatic extends NameFinder {

  def findNames(text: String): List[String] = {
    List(text)
  }
}

object ParserStatic extends ApsnetParser with NameFinderStatic {
  def parsePageIndex(doc: Document) : Iterable[String] = {
    doc >> elements(".link-item") >> attrs("href")("a")
  }

}

class ApsnetParser$Test extends FlatSpec with Matchers with NameFinderStatic {

  "Parsing Alfalfa" should "result in pathogen-host interactions for disease" in {
    val doc: Document = parse("Alfalfa.aspx")
    val interactions: Iterable[Disease] = ParserStatic.parse(doc)

    interactions should contain(Disease("Alfalfa witches’-broom", "‘Candidatus Phytoplasma asteris’", "Diseases of Alfalfa (Medicago sativa L.)"))
    interactions should contain(Disease("Red clover vein mosaic", "Red clover vein mosaic virus (RCVMV)", "Diseases of Alfalfa (Medicago sativa L.)"))
  }

  "Parsing Peach and Nectarine" should "result in pathogen-host interactions for disease" in {
    val doc: Document = parse("PeachandNectarine.aspx")
    val interactions: Iterable[Disease] = ParserStatic.parse(doc)

    interactions should contain(Disease("Bacterial canker", "Pseudomonas syringae pv. syringae van Hall 1902", "Diseases of Peach and Nectarine Peach: Prunus persica (L.) Batsch Nectarine: P. persica var. nucipersica (Suckow) C.K. Schneid."))
    interactions should contain(Disease("Bacterial canker", "Pseudomonas syringae pv. syringae van Hall 1902", "Diseases of Peach and Nectarine Peach: Prunus persica (L.) Batsch Nectarine: P. persica var. nucipersica (Suckow) C.K. Schneid."))

    interactions should contain(Disease("Green fruit rot", "Monilinia fructicola (G. Wint.) Honey", "Diseases of Peach and Nectarine Peach: Prunus persica (L.) Batsch Nectarine: P. persica var. nucipersica (Suckow) C.K. Schneid."))
    interactions should contain(Disease("Green fruit rot", "M. laxa (Aderhold & Ruhland) Honey", "Diseases of Peach and Nectarine Peach: Prunus persica (L.) Batsch Nectarine: P. persica var. nucipersica (Suckow) C.K. Schneid."))
  }

  "scrubbing name" should "remove whitespaces" in {
    val stringWithWeirdwhitespaces: String = "Mycoleptodiscus terrestris (Gerd.) Ostaz.           (syn. Leptodiscus terrestris Gerd.)"
    ParserStatic.scrub(stringWithWeirdwhitespaces) should be("Mycoleptodiscus terrestris (Gerd.) Ostaz. (syn. Leptodiscus terrestris Gerd.)")
  }

  "Parsing disease index" should "result in a list of urls to species pages" in {
    val doc: Document = parse("default.aspx")
    val links = ParserStatic.parsePageIndex(doc)

    links should contain("http://www.apsnet.org/publications/commonnames/Pages/AfricanDaisy.aspx")
    links should contain("http://www.apsnet.org/publications/commonnames/Pages/PeachandNectarine.aspx")
  }

  "get all pages" should "produce a giant list of disease interactions" in {
    val doc: Document = JsoupBrowser().get("http://www.apsnet.org/publications/commonnames/Pages/default.aspx")
    val pages = ParserStatic.parsePageIndex(doc)
    val diseases = pages.flatMap(page => {
      println(s"processing of [$page] started...")
      val pageDoc = JsoupBrowser().get(page)
      val results = ParserStatic.parse(pageDoc)
      println(s"processing of [$page] done.")
      results
    })

    diseases should contain(Disease("Bacterial canker", "Pseudomonas syringae pv. syringae van Hall 1902", "Diseases of Peach and Nectarine Peach: Prunus persica (L.) Batsch Nectarine: P. persica var. nucipersica (Suckow) C.K. Schneid."))
    diseases should contain(Disease("Alfalfa witches’-broom", "‘Candidatus Phytoplasma asteris’", "Diseases of Alfalfa (Medicago sativa L.)"))

    println(s"found ${diseases.size} disease interactions:")
    println("disease\tpathogen\thost")
    diseases.foreach(disease => {
      s"${disease.name}\t${disease.pathogen}\t${disease.host}"
    })
  }

  def parse(resourceName: String): Document = {
    JsoupBrowser().parseFile(new File(classOf[ApsnetParser$Test].getResource(resourceName).getFile))
  }
}