package org.planteome.samara

import java.text.SimpleDateFormat
import java.util.Date

import net.ruippeixotog.scalascraper.model.Document

import scala.util.{Failure, Success}


object ScraperApsnet extends Scraper with ResourceUtil {

  object Parser extends ParserApsnet with TermFinderProxy

  override def scrape() = {
    println("disease_name\tsource_taxon_verbatim_name\tsource_taxon_name\tsource_taxon_id\tinteraction_type_label\tinteraction_type_id\ttarget_taxon_verbatim_name\ttarget_taxon_name\ttarget_taxon_id\ttarget_part_name\ttarget_part_id\tsource_citation\tsource_url\tsource_accessed_at")
    scrapeDiseases().foreach {
      case (page: String, accessedAt: String, disease: Disease) => {
        val citationFull = s"${disease.citation} Accessed on $accessedAt at $page"
        println(s"${disease.name}\t${disease.verbatimPathogen}\t${disease.pathogen}\t${disease.pathogenId}\tpathogen of\thttp://purl.obolibrary.org/obo/RO_0002556\t${disease.verbatimHost}\t${disease.host}\t${disease.hostId}\t${disease.hostPartName}\t${disease.hostPartId}\t$citationFull\t$page\t$accessedAt")
      }
    }

  }

  def scrapeDiseases(): Iterable[(String, String, Disease)] = {
    val prefix = "https://www.apsnet.org/edcenter/resources/"
    getTry(s"$prefix/commonnames/Pages/default.aspx") match {
      case Success(doc) =>
        Parser.parsePageIndex(doc)
          .filterNot(_.contains("Turfgrasses"))
          .filterNot(_.contains("Conifer"))
          .filterNot(_.contains("Kalanchoë"))
          .flatMap(page => {
            Parser.parse(get(page)).map((page, today, _))
          })
      case Failure(e) => Seq.empty[(String, String, Disease)]
    }
  }

  def today: String = {
    new SimpleDateFormat("yyyy-MM-dd").format(new Date())
  }
}
