package org.planteome.samara

import net.ruippeixotog.scalascraper.model.Document
import org.scalatest._


object ParserApsnetStatic$ extends ParserApsnet with NameFinderStatic

object expectedCitations {
  val alfalfa = "D. A. Samac, L. H. Rhodes, and W. O. Lamp, primary collators (Last update: 6/25/14). Diseases of Alfalfa (Medicago sativa L.). The American Phytopathological Society."
  val peachAndNectarine = "J. K. Uyemoto, J. M. Ogawa, and B. A. Jaffee, primary collators; updated by J. E. Adaskaveg, S. W. Scott, and H. Scherm (last update 5/23/01). Diseases of Peach and Nectarine Peach: Prunus persica (L.) Batsch Nectarine: P. persica var. nucipersica (Suckow) C.K. Schneid.. The American Phytopathological Society."

}

class ParserApsnet$Test extends FlatSpec with Matchers with NameFinderStatic with ParseTestUtil {
  "Parsing Alfalfa" should "result in pathogen-host interactions for disease" in {
    val doc: Document = parse("apsnet/Alfalfa.aspx")
    val interactions: Iterable[Disease] = ParserApsnetStatic$.parse(doc)

    interactions should contain(Disease(name = "Alfalfa witches’-broom", pathogen = "‘Candidatus Phytoplasma asteris’", host = "Medicago sativa L.", citation = expectedCitations.alfalfa))
    interactions should contain(Disease("Red clover vein mosaic", "Red clover vein mosaic virus (RCVMV)", "Medicago sativa L.", citation = expectedCitations.alfalfa))
  }

  "Parsing Peach and Nectarine" should "result in pathogen-host interactions for disease" in {
    val doc: Document = parse("apsnet/PeachandNectarine.aspx")
    val interactions: Iterable[Disease] = ParserApsnetStatic$.parse(doc)

    interactions should contain(Disease(name = "Bacterial canker", pathogen = "Pseudomonas syringae pv. syringae van Hall 1902", host = "Prunus persica", citation = expectedCitations.peachAndNectarine))
    interactions should contain(Disease(name = "Bacterial canker", pathogen = "Pseudomonas syringae pv. syringae van Hall 1902", host = "Prunus persica var. nucipersica", citation = expectedCitations.peachAndNectarine))
    interactions should contain(Disease(name = "Bacterial canker", pathogen = "Pseudomonas syringae pv. syringae van Hall 1902", host = "Prunus persica", citation = expectedCitations.peachAndNectarine))
    interactions should contain(Disease(name = "Bacterial canker", pathogen = "Pseudomonas syringae pv. syringae van Hall 1902", host = "Prunus persica var. nucipersica", citation = expectedCitations.peachAndNectarine))

    interactions should contain(Disease(name = "Green fruit rot", pathogen = "Monilinia fructicola (G. Wint.) Honey", host = "Prunus persica", citation = expectedCitations.peachAndNectarine))
    interactions should contain(Disease(name = "Green fruit rot", pathogen = "Monilinia fructicola (G. Wint.) Honey", host = "Prunus persica var. nucipersica", citation = expectedCitations.peachAndNectarine))
    interactions should contain(Disease(name = "Green fruit rot", pathogen = "Monilinia laxa (Aderhold & Ruhland) Honey", host = "Prunus persica", citation = expectedCitations.peachAndNectarine))
    interactions should contain(Disease(name = "Green fruit rot", pathogen = "Monilinia laxa (Aderhold & Ruhland) Honey", host = "Prunus persica var. nucipersica", citation = expectedCitations.peachAndNectarine))

    interactions should contain(Disease(name = "Line pattern", pathogen = "Prunus necrotic ringspot virus", host = "Prunus persica", citation = expectedCitations.peachAndNectarine))
    interactions should contain(Disease(name = "Line pattern", pathogen = "Prunus necrotic ringspot virus", host = "Prunus persica var. nucipersica", citation = expectedCitations.peachAndNectarine))

    interactions should contain(Disease(name = "Rosette and decline", pathogen = "Prune dwarf virus", host = "Prunus persica var. nucipersica", citation = expectedCitations.peachAndNectarine))
  }

  "scrubbing name" should "remove whitespaces" in {
    val stringWithWeirdwhitespaces: String = "Mycoleptodiscus terrestris (Gerd.) Ostaz.           (syn. Leptodiscus terrestris Gerd.)"
    ParserApsnetStatic$.scrub(stringWithWeirdwhitespaces) should be("Mycoleptodiscus terrestris (Gerd.) Ostaz. (syn. Leptodiscus terrestris Gerd.)")
  }

  "Parsing disease index" should "result in a list of urls to species pages" in {
    val doc: Document = parse("apsnet/default.aspx")
    val links = ParserApsnetStatic$.parsePageIndex(doc)

    links should contain("http://www.apsnet.org/publications/commonnames/Pages/AfricanDaisy.aspx")
    links should contain("http://www.apsnet.org/publications/commonnames/Pages/PeachandNectarine.aspx")
  }

  "extract names" should "produce a list of extracted hostnames" in {
    val hostNames: String = "Diseases of Peach and Nectarine Peach: Prunus persica (L.) Batsch Nectarine: P. persica var. nucipersica (Suckow) C.K. Schneid."
    ParserApsnetStatic$.extractHostNames(hostNames) should contain("Prunus persica")
    ParserApsnetStatic$.extractHostNames(hostNames) should contain("Prunus persica var. nucipersica")
  }

  "extract names" should "produce a list of extracted pathogens" in {
    val pathogenNames: String = "Genus Allexivirus; Garlic viruses A-D (GVA, GVB, GVC, GVD), Garlic virus X (GVX), Garlic mite-borne mosaic virus (GMbMV), Shallot virus X (ShVX)"
    ParserApsnetStatic$.extractPathogenNames(pathogenNames) should contain("Garlic virus X")
    ParserApsnetStatic$.extractPathogenNames(pathogenNames) should contain("Garlic virus A")
  }

  "extract names" should "produce a list of extracted pathogens without commas" in {
    val pathogenNames: String = "Genus Begomovirus: Sweet potato leaf curl Canary virus (SPLCCaV) Sweet potato leaf curl China virus (SPLCV-CN) Sweet potato leaf curl Georgia virus (SPLCGV) Sweet potato leaf curl Lanzarote virus (SPLCLaV) Sweet potato leaf curl South Carolina virus (SPLCSCV) Sweet potato leaf curl Spain virus (SPLCESV) Sweet potato leaf curl Uganda virus (SPLCUV) Sweet potato leaf curl virus (SPLCV)"
    ParserApsnetStatic$.extractPathogenNames(pathogenNames) should contain("Sweet potato leaf curl Canary virus")
    ParserApsnetStatic$.extractPathogenNames(pathogenNames) should contain("Sweet potato leaf curl Spain virus")
  }

  "extract names" should "produce a list of extracted pathogens with hyphens" in {
    val pathogenNames: String = "Genus Luteovirus; Barley yellow dwarf virus–kerII, Barley yellow dwarf virus–kerIII, Barley yellow dwarf virus–MAV, Barley yellow dwarf virus–PAS, Barley yellow dwarf virus–PAV"
    ParserApsnetStatic$.extractPathogenNames(pathogenNames) should contain("Barley yellow dwarf virus–kerIII")
    ParserApsnetStatic$.extractPathogenNames(pathogenNames) should contain("Barley yellow dwarf virus–PAV")
  }

  "extract names" should "produce a list of extracted pathogens lower case" in {
    val pathogenNames: String = "genus Tobravirus, Tobacco rattle virus (TRV)"
    ParserApsnetStatic$.extractPathogenNames(pathogenNames) should contain("Tobacco rattle virus")
  }

  "extract names" should "produce a list of extracted pathogens no comma" in {
    val pathogenNames: String = "genus Potexvirus Wineberry latent virus (WLV)"
    ParserApsnetStatic$.extractPathogenNames(pathogenNames) should contain("Wineberry latent virus")
  }

  "extract names" should "and ignore stop words" in {
    val pathogenNames: String = "Other species include: P. scribneri Steiner"
    ParserApsnetStatic$.extractPathogenNames(pathogenNames) should contain("P. scribneri Steiner")
  }

  "extract names" should "and ignore +" in {
    val pathogenNames: String = "+ genus Ilarvirus, Prune dwarf virus (PDV)"
    ParserApsnetStatic$.extractPathogenNames(pathogenNames) should contain("Prune dwarf virus")
  }

"extract names" should "and parse or" in {
    val pathogenNames: String = "Caused by either genus Nepovirus, Arabis mosaic virus (ArMV) or genus Nepovirus, Strawberry latent ringspot virus (SLRV)"
    ParserApsnetStatic$.extractPathogenNames(pathogenNames) should contain("Arabis mosaic virus")
    ParserApsnetStatic$.extractPathogenNames(pathogenNames) should contain("Strawberry latent ringspot virus")
  }

"extract names" should "and parse candidate" in {
    ParserApsnetStatic$.extractPathogenNames("unassigned genus, Purple granadilla mosaic virus (PGMV)") should contain("Purple granadilla mosaic virus")
  val pathogenNames: String = "candidate Rhabdoviridae (unassigned genus), Passion fruit green spot virus (PGSV)"
  ParserApsnetStatic$.extractPathogenNames(pathogenNames) should contain("Passion fruit green spot virus")
  }

  "expand prefixes" should "fill in genus names of previously mentioned species" in {
    ParserApsnetStatic$.expandPrefixes(List("Homo sapiens", "H. sapiens", "Ariopsis felis")) should be(List("Homo sapiens", "Homo sapiens", "Ariopsis felis"))
    ParserApsnetStatic$.expandPrefixes(List("Homo sapiens", "H sapiens", "Ariopsis felis")) should be(List("Homo sapiens", "Homo sapiens", "Ariopsis felis"))
  }

}