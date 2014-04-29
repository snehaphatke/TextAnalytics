#!/bin/bash
for file in "$@"
do 
   while read word1 word2
   do
	echo "{\"_id\""":""\"""$word1""\""",""\"weighted_average\""":" "$word2" "}"
   done < "$file"
done
