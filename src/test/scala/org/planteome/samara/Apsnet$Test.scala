package org.planteome.samara

import java.io.File

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model.{Document, Element}
import org.scalatest._

import scala.collection.mutable.Stack

case class Interaction(sourceTaxon: String, interactionType: String, targetTaxon: String)

class Apsnet$Test extends FlatSpec with Matchers {

  "Parsing Alfalfa" should "result in pathogen-host interactions" in {
    val doc: Document = parse("Alfalfa.aspx")
    val header = doc >> text("h1")
    val targetTaxonOpt = header.split("[()]").zipWithIndex.filter(_._2 == 1).map(_._1).headOption

    val interactions = targetTaxonOpt match {
      case Some(targetTaxon) => {
        val pathogens = doc >> texts("dd")
        pathogens.map(pathogen => Interaction(sourceTaxon = pathogen, interactionType = "RO:0002556", targetTaxon = targetTaxon))
      }
      case _ => Seq[Interaction]()
    }

    interactions should contain(Interaction("‘Candidatus Phytoplasma asteris’","RO:0002556","Medicago sativa L."))
    interactions should contain(Interaction("Red clover vein mosaic virus (RCVMV)","RO:0002556","Medicago sativa L."))

  }

  "Parsing disease index" should "result in a list of urls to species pages" in {
    val doc: Document = parse("default.aspx")
    val links = doc >> elements(".link-item") >> attrs("href")("a")

    links should contain("http://www.apsnet.org/publications/commonnames/Pages/AfricanDaisy.aspx")
    links should contain("http://www.apsnet.org/publications/commonnames/Pages/PeachandNectarine.aspx")
  }

  def parse(resourceName: String): Document = {
    JsoupBrowser().parseFile(new File(classOf[Apsnet$Test].getResource(resourceName).getFile))
  }
}