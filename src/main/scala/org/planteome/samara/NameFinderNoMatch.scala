package org.planteome.samara

trait NameFinderNoMatch extends NameFinder {

  def findNames(text: String): List[String] = {
    List("no:match")
  }
}

