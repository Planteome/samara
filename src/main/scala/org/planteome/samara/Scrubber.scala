package org.planteome.samara

trait Scrubber {
  def scrub(str: String): String = {
    """[\s\xa0]+""".r.replaceAllIn(str, " ").trim()
  }
}
