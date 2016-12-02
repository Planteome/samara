package org.planteome.samara

import java.io.IOException

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Future
import scala.util.{Failure, Success}

class ResourceUtil$Test extends FlatSpec with Matchers with ScalaFutures with ResourceUtil {


  "requesting a valid url" should "get some content" in {
    get("https://google.com")

  }

  private val nonExistentUrl: String = "http://doesnotexist.globalbioticinteractions.org"
  "requesting an invalid url" should "throw an exception" in {
    a [IOException] should be thrownBy {
      get(nonExistentUrl)
    }
  }

  "requesting an invalid url" should "create a failed try object" in {
    val docTry = getTry(nonExistentUrl)
    docTry.isFailure should be(true)

    docTry match {
      case Success(doc) => fail("did not expect a document")
      case Failure(e) => // empty
     }
  }

}
