package org.planteome.samara

import java.io.File

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.browser.JsoupBrowser.JsoupDocument
import net.ruippeixotog.scalascraper.model.Document
import org.jsoup.helper.HttpConnection

trait ParseTestUtil {
  def parse(resourceName: String): Document = {
    JsoupBrowser().parseFile(new File(classOf[ParseTestUtil].getResource(resourceName).getFile))
  }

  def get(resourceName: String): Document = {
    println(s"[$resourceName] downloading ...")
    val twoMinutes: Int = 1000 * 120
    val doc = JsoupDocument(JsoupBrowser()
      .requestSettings(HttpConnection
          .connect(resourceName))
      .timeout(twoMinutes).get())
    println(s"[$resourceName] downloaded.")
    doc
  }
}
