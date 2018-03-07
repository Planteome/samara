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

  override lazy val taxonCachesNCBI: Seq[collection.Map[String, List[Integer]]] = {
    Console.err.println("taxonCache building...")
    val start = System.currentTimeMillis()
    val caches: Seq[collection.Map[String, List[Integer]]] = resourceNames.map(resourceName => {
      val firstFewLines: Iterator[Fun.Tuple2[String, List[Integer]]] = mapdbIterator(resourceName)
      Console.err.println(s"loading [$resourceName]...")

      val taxonCache = db.createTreeMap(resourceName)
        .pumpSource(firstFewLines.asJava)
        .pumpIgnoreDuplicates()
        .pumpPresort(100000000) // for presorting data we could also use this method
        .make[String, List[Integer]].asScala
      Console.err.println(s"loading [$resourceName] done.")
      taxonCache
    })
    val end = System.currentTimeMillis()
    Console.err.println(s"taxonCache ready [took ${(end - start) / 1000} s].")

    caches
  }

  def mapdbIterator(resourceName: String): Iterator[Fun.Tuple2[String, List[Integer]]] = {
    reducedTaxonMap(resourceName).map {
      case (key, value) =>
        new Fun.Tuple2[String, List[Integer]](key, value)
    }
  }
}

