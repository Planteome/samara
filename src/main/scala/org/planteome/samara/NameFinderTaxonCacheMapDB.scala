package org.planteome.samara

import org.mapdb.{DBMaker, Fun}

import scala.collection.JavaConverters._

trait NameFinderTaxonCacheMapDB extends NameFinderTaxonCache {

  lazy val db = {
    DBMaker
      .newTempFileDB
      .transactionDisable()
      .closeOnJvmShutdown()
      .make()
  }

  override lazy val taxonCache: collection.Map[String, List[Integer]] = {
    Console.err.println(s"taxonCache for [${taxonMapCacheConfig.name}] building...")
    val start = System.currentTimeMillis()
    val firstFewLines: Iterator[Fun.Tuple2[String, List[Integer]]] = mapdbIterator

    val taxonCache = db.createTreeMap(s"map${taxonMapCacheConfig.name}")
      .pumpSource(firstFewLines.asJava)
      .pumpIgnoreDuplicates()
      .pumpPresort(100000000) // for presorting data we could also use this method
      .make[String, List[Integer]].asScala

    val end = System.currentTimeMillis()
    Console.err.println(s"taxonCache for [${taxonMapCacheConfig.name}] ready [took ${(end - start) / 1000} s].")

    taxonCache
  }

  lazy val mapdbIterator: Iterator[Fun.Tuple2[String, List[Integer]]] = {
    reducedTaxonMap.map {
      case (key, value) =>
        new Fun.Tuple2[String, List[Integer]](key, value)
    }
  }
}

