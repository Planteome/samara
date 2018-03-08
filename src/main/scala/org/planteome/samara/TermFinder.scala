package org.planteome.samara

case class Term(name: String, id: String)

trait TermFinder {
  def findTerms(text: String): List[Term]
}

