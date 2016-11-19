package org.planteome.samara

trait IdFinder {
  def findId(id: Integer): Option[Int]
}
