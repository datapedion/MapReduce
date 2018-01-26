/*
 * Map and Reduce Paradigm - Parallel
 */
package de.datapedion.mapReduce;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.datapedion.mapReduce.common.Pair;

/**
 * The Class GenericMapReduce can execute programs structured in the map-reduce
 * paradigm for any given type.
 *
 * @param <T>
 *            the generic type
 * @param <K>
 *            the key type
 * @param <V>
 *            the value type
 */
public abstract class GenericMapReduce<T extends GenericMapReduce<T, K, V>, K, V> {

	/** store the result. */
	HashMap<K, V> result = new HashMap<K, V>();

	/**
	 * On all elements of the input stream the mapper is applied, collect the
	 * stream by the key (grouping) and reducing the result by the reducer.
	 *
	 * @param input
	 *            an input stream of <code>String</code>
	 * @param mapper
	 *            the mapper function which is applied on each element of the input
	 *            stream
	 * @param reduce
	 *            the reducer reduces the grouped values
	 * 
	 * @return the result stores the resulting values of the process
	 */
	@SuppressWarnings( "unchecked" )
	public T mapAndReduce( Stream<String> input, Function<String, Stream<Pair<K, V>>> mapper,
			Consumer<Entry<K, List<V>>> reduce ) {

		input.flatMap( mapper )
			 .collect( Collectors.groupingByConcurrent( Pair<K, V>::getKey, Collectors.mapping( Pair::getValue, Collectors.toList() ) ) )
			 .entrySet().forEach( reduce );
		return (T) this;
	}

	public HashMap<K, V> getResult() {

		return result;
	}

	public abstract Stream<String> getInputStream( String fileName )
			throws IOException;

}