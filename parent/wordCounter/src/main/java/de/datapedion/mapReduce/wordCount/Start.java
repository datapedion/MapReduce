package de.datapedion.mapReduce.wordCount;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.datapedion.mapReduce.GenericInput;
import de.datapedion.mapReduce.common.Pair;

public class Start {

	public static void main( final String[] args ) {

		Instant start = Instant.now();

		final String fileName = args[0];
		final String splitPattern = "\\s";

		// Split strings by the given splitPattern into word
		// creating Pairs of word and the count of 1
		final Function<String, Stream<Pair<String, Integer>>> mapper = t -> Pattern.compile( splitPattern ).splitAsStream( t )
				.map( s -> new Pair<String, Integer>( s, 1 ) );

		// create a MapReducer with the types String for the word and Double for counting them
		final GenericInput<String, Integer> wordCount = new GenericInput<String, Integer>();

		// reduce the grouped result by summing up the count per word
		final Consumer<Entry<String, List<Integer>>> reduce = entry -> wordCount.getResult().put( entry.getKey(),
				entry.getValue().stream().reduce( 0, Integer::sum ) );

		try {
			wordCount.mapAndReduce( wordCount.getInputStream( fileName ), mapper, reduce );
		} catch ( final IOException e ) {
			System.out.println( "file not found" );
			e.printStackTrace();
		}

		final Map<String, Integer> r = sortResult( wordCount );

		// print sorted the result
		r.entrySet().forEach( k -> System.out.println( k ) );
		Instant end = Instant.now();
		System.out.println( "Time: " + Duration.between( start, end ) );
	}

	private static LinkedHashMap<String, Integer> sortResult( final GenericInput<String, Integer> wordCount ) {

		return wordCount.getResult().entrySet().parallelStream()
				.sorted( Map.Entry.comparingByKey() ).sorted( Map.Entry.comparingByValue( Comparator.naturalOrder() ) )
				.collect( Collectors.toMap( Map.Entry::getKey, Map.Entry::getValue, ( oldValue, newValue ) -> oldValue,
						LinkedHashMap::new ) );
	}
}
