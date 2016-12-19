package org.planteome.samara

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.browser.JsoupBrowser.JsoupDocument
import net.ruippeixotog.scalascraper.model.Document
import org.jsoup.helper.HttpConnection

import scala.util.{Failure, Success, Try}

trait ResourceUtil {
  def get(url: String): Document = {
    Console.err.println(s"[$url] downloading ...")
    val twoMinutes: Int = 1000 * 120
    val doc = JsoupDocument(JsoupBrowser()
      .requestSettings(HttpConnection
        .connect(url))
      .timeout(twoMinutes).get())
    Console.err.println(s"[$url] downloaded.")
    doc
  }

  def getTry(url: String): Try[Document] = {
    try {
      Success(get(url))
    } catch {
      case e: Throwable => {
        Console.err.println(s"[$url] download failed because of:")
        e.printStackTrace(Console.err)
        Failure(e)
      }
    }

  }
}
