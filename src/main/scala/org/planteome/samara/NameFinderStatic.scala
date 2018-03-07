package org.planteome.samara

trait NameFinderStatic extends NameFinder {

  def findNames(text: String): List[String] = {
    List(s"id[$text]")
  }
}

