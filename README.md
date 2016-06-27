# samara*
A commandline tool to extract plant trait data from open data sources.

[![Build Status](https://travis-ci.org/jhpoelen/samara.svg?branch=master)](https://travis-ci.org/jhpoelen/samara)

*A samara is a winged achene, a type of fruit in which a flattened wing of fibrous, papery tissue develops from the ovary wall. (from https://en.wikipedia.org/wiki/Samara_(fruit) accessed at 2016-06-10).

## prerequisites
sbt 1.3+/java jdk 8+/git

## usage

1. clone this repo
2. build jar by ```sbt assembly```
3. list available sources by ```java -jar target/scala-2.11/samara-assembly-0.1.0.jar list```
4. scrape a apsnet and put results into ```apsnet.tsv``` by ```java -jar target/scala-2.11/samara-assembly-0.1.0.jar scrape apsnet > apsnet.tsv```
