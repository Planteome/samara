package org.planteome.samara

import java.io.File


import java.io.File

case class Config(source: String = "",
                  mode: String = "")

object Samara extends App {
  val sourceMap = Map[String, Scraper]("apsnet" -> ScraperApsnet, "ars-grin" -> ScraperGrin)
  val supportedSources = sourceMap.keys.toSeq

  val parser = new scopt.OptionParser[Config]("samara") {
    head("samara", "0.1.0")

    help("help").text("prints this usage text")

    cmd("scrape")
      .text("scrape traits from provided source")
      .action((_, c) => c.copy(mode = "scrape"))
      .children(
        arg[String]("<source>").required().action((v, c) =>
          c.copy(source = v)).text(s"(${supportedSources.mkString("|")})"),
        checkConfig(c =>
          if (c.mode == "scrape" && !supportedSources.contains(c.source)) failure(s"source [${c.source}] not supported... yet")
          else success)
      )

    cmd("list")
      .text("list available source for scraping")
      .action((_, c) => c.copy(mode = "list"))

  }

  parser.parse(args, Config()) match {
    case Some(config) =>
      config.mode match {
        case "scrape" => {
          sourceMap.get(config.source) match {
            case Some(scraper) => scraper.scrape()
            case None =>
          }
        }
        case "list" =>
          supportedSources.foreach(println)
      }

    case None =>
    // arguments are bad, error message will have been displayed
  }
}
