#!/bin/bash

echo "Word Count of the if-kippling.txt -> single-core-engine"
java -cp wordCounter-1.0.jar:single-1.0.jar:common-1.0.jar de.datapedion.mapReduce.wordCount.Start if-kippling.txt 
echo "Word Count of the Original.txt -> multi-core-engine"
java -cp wordCounter-1.0.jar:parallel-1.0.jar:common-1.0.jar de.datapedion.mapReduce.wordCount.Start if-kippling.txt
echo "Average Movie Ratings -> single-core-engine"
java -cp averageCalculator-1.0.jar:single-1.0.jar:common-1.0.jar de.datapedion.mapReduce.movies.Start ratings.txt movies.txt 
echo "Average Movie Ratings -> multi-core-engine"
java -cp averageCalculator-1.0.jar:parallel-1.0.jar:common-1.0.jar de.datapedion.mapReduce.movies.Start ratings.txt movies.txt
