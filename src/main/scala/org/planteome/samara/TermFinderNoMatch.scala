package org.planteome.samara

trait TermFinderNoMatch extends TermFinder {

  def findTerms(text: String): List[Term] = {
    List()
  }
}

