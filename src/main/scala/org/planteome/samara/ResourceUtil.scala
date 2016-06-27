package org.planteome.samara

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.browser.JsoupBrowser.JsoupDocument
import net.ruippeixotog.scalascraper.model.Document
import org.jsoup.helper.HttpConnection

trait ResourceUtil {
  def get(resourceName: String): Document = {
    Console.err.println(s"[$resourceName] downloading ...")
    val twoMinutes: Int = 1000 * 120
    val doc = JsoupDocument(JsoupBrowser()
      .requestSettings(HttpConnection
        .connect(resourceName))
      .timeout(twoMinutes).get())
    Console.err.println(s"[$resourceName] downloaded.")
    doc
  }
}
