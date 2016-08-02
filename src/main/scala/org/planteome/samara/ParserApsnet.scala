package org.planteome.samara

import java.io.File

import net.ruippeixotog.scalascraper.model.Document
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._

import scala.io.Source


// http://www.apsnet.org/publications/commonnames/Pages/default.aspx

case class Disease(name: String, pathogen: String, host: String)

abstract class ParserApsnet extends NameFinder with Scrubber {

  def parsePageIndex(doc: Document): Iterable[String] = {
    doc >> elements(".link-item") >> attrs("href")("a")
  }

  def parse(doc: Document): Iterable[Disease] = {
    val diseaseLists = doc >> elements("dt,dd")
    val elems = diseaseLists.map(elem => {
      (elem.tagName, elem >> text(elem.tagName))
    })

    val withDisease = elems
      .zipWithIndex
      .foldLeft(List.empty[String])((acc, item) => {
        item._1 match {
          case ("dt", v) => v :: acc
          case ("dd", _) => acc.head :: acc
        }
      }).reverse

    val targetTaxonNames = doc >> text("h1")

    findNames(targetTaxonNames)
      .flatMap(targetTaxon => {
        elems.zip(withDisease)
          .filter {
            case (("dd", _), _) => true
            case _ => false
          }
          .flatMap {
            case ((_, p), d) => {
              val hostNames: Seq[String] = extractHostNames(targetTaxon)
              hostNames.map { hostname => Disease(name = scrub(d), pathogen = scrub(p), host = hostname) }
            }
          }
      })
  }

  def extractHostNames(targetTaxon: String): Seq[String] = {
    val scrubbedHost: String = scrub(targetTaxon)

    val nameMap = Source.fromInputStream(getClass.getResourceAsStream("apsnetNameMap.tsv"))
      .getLines()
      .foldLeft(Map[String, String]()) {
        (agg, line) =>
          val split = line.split('\t')
          agg ++ Map(split(0) -> split(1))
      }

    nameMap.get(scrubbedHost) match {
      case Some(name) => {
        name.split('|')
      }
      case _ => singleHostname(scrubbedHost)
    }

  }

  def singleHostname(scrubbedHost: String): Seq[String] = {
    val singleName = """[^:]*\((.*)\).*""".r
    Seq(scrubbedHost match {
      case singleName(name) => name
      case _ => scrubbedHost
    })
  }
}
