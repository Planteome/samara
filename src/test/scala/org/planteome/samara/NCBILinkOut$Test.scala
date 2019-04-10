package org.planteome.samara

import java.io.InputStream
import java.net.URL

import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source
import scala.xml.{NodeSeq, XML}


class NCBILinkOut$Test extends FlatSpec with Matchers with NCBILinkOut {

  "id finder" should "map a ncbi taxon to a grin taxon id" in {
    val ids: Seq[Int] = findLinkOutForNCBITaxonId(3879)
    ids should contain(300359)
  }

  "id extractor" should "dig grintaxon ids out of linkout xml" in {
    val is: InputStream = getClass.getResourceAsStream("ncbi/medicagoSativaLinks.xml")
    val grinTaxonIds: Seq[Int] = parseGRINTaxonIds(is)
    grinTaxonIds should contain(300359)
    grinTaxonIds should contain(23676)
  }

  "ids for grin" should "return ncbi taxon ids with grin linkouts" in {
    findNCBITaxonIdsWithGRINTaxonLinkOut should contain(3879)
  }

  "id map builder" should "build a map linking grin taxon id with ncbi ids" in {
    val ids: Seq[Int] = findNCBITaxonIdsWithGRINTaxonLinkOut
    val ncbiTaxonIdLookup = buildIdMap(ids.take(1))

    ncbiTaxonIdLookup.get(30022) should be(Some(329890))
    ncbiTaxonIdLookup.get(3025) should be(Some(329890))
  }


  "id extractor" should "extract id from url" in {
    val id = extractIdFromUrl("https://npgsweb.ars-grin.gov/gringlobal/taxonomydetail.aspx?30022")
    id should be(Some(30022))
  }

}
