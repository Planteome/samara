package org.planteome.samara

import org.mapdb.{DBMaker, Fun}

import scala.collection.JavaConverters._
import scala.util.Random

trait TermFinderTaxonCacheMapDBStatic extends TermFinderTaxonCacheMapDB {

  override lazy val mapdbIterator: Iterator[Fun.Tuple2[String, List[Integer]]] = {
    val iter = new Iterator[Fun.Tuple2[String, List[Integer]]] {
      override def hasNext: Boolean = {
        true
      }

      override def next(): Fun.Tuple2[String, List[Integer]] = {
        new Fun.Tuple2[String, List[Integer]](Random.nextString(50), List(1, 2))
      }
    }
    iter.slice(0, 10000)
  }
}

