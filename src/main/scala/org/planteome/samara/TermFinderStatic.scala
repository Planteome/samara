package org.planteome.samara

trait TermFinderStatic extends TermFinder {

  def findTerms(text: String): List[Term] = {
    List(Term(text, s"id[$text]"))
  }
}

