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

    val diseases = findNames(targetTaxonNames)
      .flatMap(targetTaxon => {
        elems.zip(withDisease)
          .filter {
            case (("dd", _), _) => true
            case _ => false
          }
          .flatMap {
            case ((_, pathogenName), diseaseName) => {
              val hostNames: Seq[String] = extractHostNames(targetTaxon)
              hostNames.map { hostname => Disease(name = scrub(diseaseName), pathogen = scrub(pathogenName), host = hostname) }
            }
          }
      })

    diseases zip expandPrefixes(diseases.map(_.pathogen)) map {
      case ((disease, pathogenExpanded)) => Disease(name = disease.name, pathogen = pathogenExpanded, host = disease.host)
    }
  }

  def expandPrefixes(names: List[String]): List[String] = {
    names.foldLeft((names.headOption.getOrElse(""), List[String]())) { (acc, name) =>
      val abbreviated = """(^\w*\.)\s+.*""".r
      name match {
        case abbreviated(abbr) => (acc._1, name.replaceFirst(abbr, acc._1) :: acc._2)
        case str => (str.split("\\s").head, name :: acc._2)
      }
    }._2.reverse
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
