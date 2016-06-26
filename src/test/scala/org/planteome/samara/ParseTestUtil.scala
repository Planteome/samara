package org.planteome.samara

import java.io.File

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document

trait ParseTestUtil {
  def parse(resourceName: String): Document = {
    JsoupBrowser().parseFile(new File(classOf[ParseTestUtil].getResource(resourceName).getFile))
  }
}
