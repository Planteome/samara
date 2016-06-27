package org.planteome.samara

import net.ruippeixotog.scalascraper.model.Document



object ScraperApsnet extends Scraper with ResourceUtil with NameFinderStatic {

  object Parser extends ParserApsnet with NameFinderStatic

  def scrape() = {
    println("disease\tpathogen\thost")
    scrapeDiseases().foreach(disease => {
      println(s"${disease.name}\t${disease.pathogen}\t${disease.host}")
    })

  }

  def scrapeDiseases(): Iterable[Disease] = {
    val doc: Document = get("http://www.apsnet.org/publications/commonnames/Pages/default.aspx")
    val pages = Parser.parsePageIndex(doc)
    pages.flatMap(page => {
      Parser.parse(get(page))
    })
  }
}
