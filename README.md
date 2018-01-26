# map-reduce engine

An implementation of map-reduce engines using Java 8 Streams.
Allowing programs structured in the map-reduce paradigm to be executed. 

Two engines are available:
- single-core (single-1.0.jar)
- multi-core  (parallel-1.0.jar)

Both engines share a common code (common-1.0.jar).

Two test programs are available:
- word-count (wordCounter-1.0.jar)
- averageRating (averageCalculator-1.0.jar) 

By exchanging the engine, either program can be run on either engine without changes.


### prerequisites

For running the program
- install java 8 
For building the project
- install maven 


### How to: run the programs

For a quick look at the output in a Linux environment

```
cd into examples folder and run the script run_all.sh.

```

For a detailed look at the output, start individual programs and alternate libraries use the following syntax:
java -cp program:engine:common main_class path/to/file path/to/file


All files should be in the same folder. The WordCounter program needs a text file as parameter. 


Word Count of the if-kippling.txt run by single-core-engine: 

```
java -cp wordCounter-1.0.jar:single-1.0.jar:common-1.0.jar de.datapedion.mapReduce.wordCount.Start if-kippling.txt 

```

Word Count of the if-kippling.txt run by multi-core-engine:

```
java -cp wordCounter-1.0.jar:parallel-1.0.jar:common-1.0.jar de.datapedion.mapReduce.wordCount.Start if-kippling.txt

```


The averageRating program need two text files. First the ratings file and second the movie files.


Average Movie Ratings of ratings.txt and movie.txt run by single-core-engine:

```
java -cp averageCalculator-1.0.jar:single-1.0.jar:common-1.0.jar de.datapedion.mapReduce.movies.Start ratings.txt movies.txt 
```


Average Movie Ratings of ratings.txt and movie.txt run by multi-core-engine:

```
java -cp averageCalculator-1.0.jar:parallel-1.0.jar:common-1.0.jar de.datapedion.mapReduce.movies.Start ratings.txt movies.txt
```


On Windows replace the classpath seperator ":" with ";"

```
java -cp wordCounter-1.0.jar;common-1.0.jar;single-1.0.jar de.datapedion.mapReduce.wordCount.Start fileName

```

### How to: build the project

The project was build with maven. Run the following goals:

```
maven clean install
```

### Known Issues

- No help or manual files
- No failure or exception handling 
- Documentation is basic


## Software used

* [JAVA](https://java.com/en/download/) 
* [Maven](https://maven.apache.org/) - Dependency Management


### Performance

Running the programs with multiple cores does not lead to significantly faster results. To investigate this behavior, different input files were generated: a) 1 Mio. lines á 3 words vs. b) 3 lines á 1 Mio. words. The latter (=longer lines) resulted in higher performance gain. In any case, the multi-engine was max. 30% faster than the single-core-engine.
Possible reason for this behavior include:

a) the tasks are so small that the time necessary for distribution between the cores has a comparatively big impact 
b) its an inherent issue of Java 8 Streams
c) the potential of Java 8 is not fully used
d) the partition of the data must be improved
e) single-threading of the reduce step

A next step in optimization for a-d) would be to use a profiler for more precise monitoring of the results. 
For e) parallelization of the reduce step should be tested. 








