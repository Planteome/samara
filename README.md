# samara*
A commandline tool to extract plant trait data from [open data sources](sources.md).

[![Build Status](https://travis-ci.org/Planteome/samara.svg?branch=master)](https://travis-ci.org/Planteome/samara) [![DOI](https://zenodo.org/badge/60880220.svg)](https://zenodo.org/badge/latestdoi/60880220)

Plants trait data from recent automated scrapes are available at [APSNet](https://build.berkeleybop.org/view/Planteome/job/extract-apsnet-diseases/) or [ARS-GRIN](https://build.berkeleybop.org/view/Planteome/job/extract-grin-traits/). 

For more information about APSNet scraping process, including name matching, please go [here](src/main/resources/org/planteome/samara/apsnet).

*A samara is a winged achene, a type of fruit in which a flattened wing of fibrous, papery tissue develops from the ovary wall. (from https://en.wikipedia.org/wiki/Samara_(fruit) accessed at 2016-06-10).

## prerequisites
sbt 0.13.8+/java jdk 8+/git/maven 3.3+

## build/test
1. clone this repo
2. build jar by ```sbt assembly```: a stand-alone jar ```samara-assembly-[version].jar``` will be available in ```target/scala-2.11/```
3. run tests by ```sbt test```

## download
Don't like building your own jar? Go to [releases](https://github.com/Planteome/samara/releases), pick a release and download the jar from there. 


## usage
1. list available sources by ```java -jar samara-assembly-[version].jar list```
2. scrape a source called ```apsnet``` and put results into ```apsnet.tsv``` by ```java -jar samara-assembly-[version].jar scrape apsnet > apsnet.tsv```
