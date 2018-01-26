package de.datapedion.mapReduce.movies;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import de.datapedion.mapReduce.GenericInput;
import de.datapedion.mapReduce.common.Pair;

public class Start {

	private static String FILENOTFOUND = "file not found ";

	public static void main( String[] args ) {
		Instant start = Instant.now();
		
		
		String ratingsFileName = args[0];
		String moviesFileName = args[1];

		final String datasetSplit = ",";
		final String resultSplit = " ";

		final String regex_decimal = "[^\\d\\.]";
		final String regex_name = "name: |'|\\}";

		// First: Clean the ratingsFile and Calculate the average Ratings

		// this Function splits a given Stream of Strings
		// into Pairs containing the ID of a movie (Integer) and the corresponding
		// rating (Double)

		Function<String, Stream<Pair<Integer, Double>>> stringMapper = s -> Stream.of( new Pair<Integer, Double>(
				Integer.parseInt( s.split( datasetSplit )[1].replaceAll( regex_decimal, "" ).trim() ),
				Double.parseDouble( s.split( datasetSplit )[2].replaceAll( regex_decimal, "" ).trim() ) ) );

		// The Generic MapReducer is instantiated by the types needed (Movie_id:Integer,
		// Rating:Double)
		// The implementation of the GenericInput is external.
		// By choosing the implementation the execution will be parallel or not
		GenericInput<Integer, Double> average = new GenericInput<Integer, Double>();

		// The consumer will reduce for each movie-id the ratings by calculating the
		// average
		Consumer<Entry<Integer, List<Double>>> reduce_avg = entry -> average.getResult().put( entry.getKey(),
				entry.getValue().stream().reduce( 0.0, ( sum, x ) -> ( sum + x ) ) / entry.getValue().size() );

		// Use the mapper and the reducer and store the result in the GenericeInput
		// Object
		try {
			average.mapAndReduce( average.getInputStream( ratingsFileName ), stringMapper, reduce_avg );
		} catch ( IOException e ) {
			System.out.println( FILENOTFOUND + ratingsFileName );
			e.printStackTrace();
		}

		// Second clean the movie files

		// this Function splits a given Stream of Strings
		// into Pairs containing the ID of a movie (Integer) and
		// corresponding names (String)
		final Function<String, Stream<Pair<Integer, String>>> mapper_movie = s -> Stream.of( new Pair<Integer, String>(
				Integer.parseInt( s.split( datasetSplit )[0].replaceAll( regex_decimal, "" ).trim() ),
				s.split( datasetSplit )[1].replaceAll( regex_name, "" ).trim() ) );

		GenericInput<Integer, String> movie = new GenericInput<Integer, String>();

		Consumer<Entry<Integer, List<String>>> reduce = entry -> movie.getResult().put( entry.getKey(),
				entry.getValue().stream().reduce( "", String::concat ) );

		// Get the movienames and IDs
		try {
			movie.mapAndReduce( movie.getInputStream( moviesFileName ), mapper_movie, reduce );
		} catch ( IOException e ) {
			e.printStackTrace();
		}

		// Combine both sources by converting their result into streams.
		// Combine the streams by concatting them

		Stream<String> combinedData = Stream
				.concat( movie.getResult().entrySet().stream(), average.getResult().entrySet().stream() )
				.<String> map( e -> e.getKey() + datasetSplit + e.getValue() );

		final Function<String, Stream<Pair<String, String>>> mapper_combine = s -> Stream
				.of( new Pair<String, String>( s.split( datasetSplit )[0], s.split( datasetSplit )[1] ) );

		GenericInput<String, String> combine = new GenericInput<String, String>();

		Consumer<Entry<String, List<String>>> reduceNamesAndRatings = entry -> combine.getResult().put( entry.getKey(),
				entry.getValue().stream().reduce( ( a, b ) -> a + resultSplit + b ).get() );

		combine.mapAndReduce( combinedData, mapper_combine, reduceNamesAndRatings );

		// Print the result
		combine.getResult().values().forEach( k -> System.out.println( k ) );
		
		Instant end = Instant.now();
		System.out.println("Time: " + Duration.between(start, end) );

	}
}
