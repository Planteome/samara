package org.planteome.samara

import java.text.SimpleDateFormat
import java.util.Date

import net.ruippeixotog.scalascraper.model.Document


object ScraperApsnet extends Scraper with ResourceUtil with NameFinderStatic {

  object Parser extends ParserApsnet with NameFinderStatic

  override def scrape() = {
    println("diseaseName\tsourceTaxonName\tinteractionTypeLabel\tinteractionTypeId\ttargetTaxonName\tsourceCitation\tsourceUrl\tsourceAccessedAt")
    scrapeDiseases().foreach {
      case (page: String, accessedAt: String, disease: Disease) => {
        val citationFull = s"${disease.citation} Accessed on $accessedAt at $page"
        println(s"${disease.name}\t${disease.pathogen}\tpathogen of\thttp://purl.obolibrary.org/obo/RO_0002556\t${disease.host}\t${citationFull}\tpage\taccessedAt")
      }
    }

  }

  def scrapeDiseases(): Iterable[(String, String, Disease)] = {
    val doc: Document = get("http://www.apsnet.org/publications/commonnames/Pages/default.aspx")
    val pages = Parser.parsePageIndex(doc)
    pages.flatMap(page => {
      Parser.parse(get(page)).map((page, today, _))
    })
  }

  def today: String = {
    new SimpleDateFormat("yyyy-MM-dd").format(new Date())
  }
}
