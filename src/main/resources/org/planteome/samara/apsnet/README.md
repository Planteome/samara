created with something like: ```cat taxonUnmatched.tsv  | grep apsnet\.org | awk -F '\t' '{ print $1 }' | sort | uniq > nameMismatches.tsv```. This file can then be used to populate the name/taxon mapping files. For more infomation see https://github.com/jhpoelen/eol-globi-data/wiki/Taxonomy-Matching . 


See https://github.com/jhpoelen/samara for more information.
