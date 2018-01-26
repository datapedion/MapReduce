/*
 * This it the parallel version
 * The package name for single and parallel execution is the same
 */
package de.datapedion.mapReduce;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * The Class GenericInput is instantiated by the types used by the specific program
 *
 * @param <K>
 *            the key or firstType (for example a String for a "Word")
 * @param <V>
 *            the value or secondType (for example an Integer for count like "1")
 * 
 *            By extending GenericMapReduce this class can use the MapAndReduce-Function to process the input stream.
 * 
 *            This is the parallel stream input version
 */
public class GenericInput<K, V> extends GenericMapReduce<GenericInput<K, V>, K, V> {

	/*
	 * Get a parallel stream from a given fileName
	 */
	@Override
	public Stream<String> getInputStream( String fileName )
			throws IOException {

		return Files.lines( Paths.get( fileName ) ).parallel();
	}

}