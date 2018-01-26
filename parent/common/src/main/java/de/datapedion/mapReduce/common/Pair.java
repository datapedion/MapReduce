/*
 * The generice Class is used to store results during the MapAndReduce
 * This package contains the code used by both version of the engine
 */
package de.datapedion.mapReduce.common;

/**
 * The Class Pair can hold a pair of any given type
 *
 * @param <K>
 *            the key type or the first type
 * @param <V>
 *            the value type or the second type
 */
public class Pair<K, V> {

	final private K	key;
	final private V	value;

	public Pair( K key, V value ) {

		this.key = key;
		this.value = value;
	}

	public K getKey() {

		return key;
	}

	public V getValue() {

		return value;
	}

	/*
	 * For debugging the toString method is overwritten
	 */
	public String toString() {

		return this.key + "->" + this.value;
	}
}
