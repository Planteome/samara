# Samara APSNet Name and Taxon Mapping

In order to extract machine readable information from human readable pages at apsnet.org,
the following process in implemented:

### Process

1. samara extracts host-pathogen interactions from http://www.apsnet.org/publications/commonnames/Pages/default.aspx and associated pages
2. resulting name strings are mapped (one-to-{none|one|many}) using nameMap.tsv
3. the mapped names are now linked to NCBITaxon ids using taxonMap.tsv
4. remaining mapped names are linked using GloBI's taxon map (e.g. http://doi.org/10.5281/zenodo.265895)
5. samara apsnet output is generated via stdout and published as tsv file (e.g. https://github.com/jhpoelen/samara/releases/tag/v0.1.8)
6. GloBI integrates the published apsnet and performs additional name linking and term matching
7. samara's apsnet plant-disease interactions are now discoverable through http://globalbioticinteractions.org to facilitate use and error detection

### Files

nameMap.tsv: a two column maps name strings to no (i.e. "no:name"), one (e.g. "Homo sapiens"),
or more pipe delimited name strings (e.g. "Homo sapiens|Homo ludens").

taxonMap.tsv: a four column taxon map (provided id/name, resolved id/name) to map given id/name pairs
to desired id/name pairs. Id prefixes are currently inspired by http://api.globalbioticinteractions.org/prefixes
and also uses the commonly used NCBITaxon: prefix.

Both files are currently managed in GitHub and pull requests / commits can be made to add,
change or removing name/taxon mappings.

### Name reports

Because samara's apsnet is integrated with GloBI, the taxon matching reports can be re-used. For instance, a list of all distinct unmatched names from apsnet can be created with: ```cat taxonUnmatched.tsv  | grep apsnet\.org | awk -F '\t' '{ print $1 }' | sort | uniq > nameMismatches.tsv```. For more infomation see https://github.com/jhpoelen/eol-globi-data/wiki/Taxonomy-Matching . 


See https://github.com/jhpoelen/samara for more information.
