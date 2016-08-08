package org.planteome.samara

trait Scrubber {
  def scrub(str: String): String = {
    val scrubbed = """[\s\xa0]+""".r.replaceAllIn(str, " ").trim
    """([Oo]ther)|(species)|(include:)""".r.replaceAllIn(scrubbed, " ").trim
  }
}
