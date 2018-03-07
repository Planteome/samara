lazy val commonSettings = Seq(
  organization := "org.planteome",
  version := "0.1.10",
  scalaVersion := "2.12.1"
)

val taxonCacheModule = "org.eol" % "eol-globi-datasets" % "0.6" artifacts Artifact("eol-globi-datasets", "zip", "zip").copy(classifier = Some("taxa"))
val ncbiLinkOutModule = "org.planteome.samara" % "ncbi-linkout-map" % "0.1.8" artifacts Artifact("ncbi-linkout-map", "gz", "tsv.gz").copy(url = Some(new java.net.URL("https://github.com/jhpoelen/samara/releases/download/v0.1.9/grin-ncbi_linkout.tsv.gz")))
val plantOntologyModule = "org.planteome" % "plant-ontology" % "0.0.1" artifacts Artifact("plant-ontology", "zip", "zip").copy(url = Some(new java.net.URL("https://github.com/Planteome/plant-ontology/archive/master.zip")))

lazy val installLinkOutMap = taskKey[Seq[java.io.File]]("install LinkOut map in resources")

installLinkOutMap := {
  val log: Logger = streams.value.log
  log.info("NCBI Taxonomy LinkOut installing...")
  val targetDir = (resourceManaged in Compile).value / "org" / "planteome" / "samara"
  val targetModule = ncbiLinkOutModule

  IO.createDirectory(targetDir)

  val taxonArchive = (update in Compile).value
    .filter { (module: ModuleID) => {
      module.toString() == targetModule.toString()
    }
    }.select(configurationFilter("compile")).headOption

  val extracted = taxonArchive match {
    case Some(archiveFilename) => {
      val targetFile = new java.io.File(targetDir, "taxonMap.tsv.gz")
      log.info(s"copying [$archiveFilename] to [$targetFile]...")
      IO.copy(Seq((archiveFilename, targetFile)))
    }
    case None => {
      toError(Some(s"no archive found for [$targetModule]."))
      Set()
    }
  }
  log.info("NCBI Taxonomy LinkOut installed.")
  extracted.toSeq
}

lazy val installTaxonCache = taskKey[Seq[java.io.File]]("install taxon cache in resources")

installTaxonCache := {
  val log: Logger = streams.value.log
  log.info("taxon cache installing...")
  val targetDir = (resourceManaged in Compile).value / "org" / "eol" / "globi" / "taxon"
  val targetModule = taxonCacheModule

  IO.createDirectory(targetDir)

  val taxonArchive = (update in Compile).value
    .filter { (module: ModuleID) => {
      module.toString() == targetModule.toString()
    }
    }.select(configurationFilter("compile")).headOption

  val extracted = taxonArchive match {
    case Some(archiveFilename) => {
      log.info(s"[$targetModule] unpacking to [$targetDir]...")
      IO.unzip(archiveFilename, targetDir)
    }
    case None => {
      toError(Some(s"no archive found for [$targetModule]."))
      Set()
    }
  }
  log.info("GloBI Taxon Cache installed.")
  extracted.toSeq.filter(_.getName.contains("taxonMap"))
}
lazy val installPlantOntology = taskKey[Seq[java.io.File]]("install plant ontology in resources")

installPlantOntology := {
  val log: Logger = streams.value.log
  log.info("plant ontology installing...")
  val targetDir = (resourceManaged in Compile).value / "org" / "planteome" / "plant-ontology"
  val targetModule = plantOntologyModule

  IO.createDirectory(targetDir)

  val taxonArchive = (update in Compile).value
    .filter { (module: ModuleID) => {
      module.toString() == targetModule.toString()
    }
    }.select(configurationFilter("compile")).headOption

  val extracted = taxonArchive match {
    case Some(archiveFilename) => {
      log.info(s"[$targetModule] unpacking to [$targetDir]...")
      IO.unzip(archiveFilename, targetDir)
    }
    case None => {
      toError(Some(s"no archive found for [$targetModule]."))
      Set()
    }
  }
  log.info("Plant Ontology installed.")
  extracted.toSeq.filter(_.getName.contains("po.owl"))
}


lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "samara",
    resolvers ++= Seq(Resolver.sonatypeRepo("public"),
      "GloBI releases" at "https://s3.amazonaws.com/globi/release/"),
    libraryDependencies ++= Seq(
      "net.ruippeixotog" %% "scala-scraper" % "1.2.0",
      "com.github.scopt" %% "scopt" % "3.5.0",
      "org.scala-lang.modules" %% "scala-xml" % "1.0.6",
      "org.mapdb" % "mapdb" % "1.0.9",
      "org.apache.jena" % "apache-jena-libs" % "2.13.0" excludeAll(
        ExclusionRule(organization = "commons-logging"),
        ExclusionRule(organization = "org.slf4j")
        ),
      taxonCacheModule,
      ncbiLinkOutModule,
      plantOntologyModule,
      "org.scalatest" %% "scalatest" % "3.0.0" % "test"
    ),
    resourceGenerators in Compile += installTaxonCache.taskValue,
    resourceGenerators in Compile += installLinkOutMap.taskValue,
    resourceGenerators in Compile += installPlantOntology.taskValue,
    test in assembly := {}
  )
