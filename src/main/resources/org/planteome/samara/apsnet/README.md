created with something like: ```cat taxonUnmatched.tsv  | grep apsnet\.org | awk -F '\t' '{ print $1 }' | sort | uniq > nameMismatches.tsv```. This file can then be used to populate the name/taxon mapping files. Note that the taxonUnmatched.csv file contains close name matches and associated taxon information. This may be used to detect (and correct) typos and/or invalid name. For more infomation see https://github.com/jhpoelen/eol-globi-data/wiki/Taxonomy-Matching . 


See https://github.com/jhpoelen/samara for more information.
