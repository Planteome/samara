package org.planteome.samara

trait IdFinder {
  def findIds(id: String): List[String]
}
