package de.datapedion.mapReduce.wordCount;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.stream.Stream;

public class GenerateFile {

	public static void main( String[] args ) {

		String[] words = { "one", "two", "two", "three", "three", "three" };

		int lines = 10000000;
		if ( args.length == 1 ) {
			lines = Integer.parseInt( args[0].trim() );
		}
		String fileName = "test_big.txt";

		generateFile( fileName, words, lines );

	}

	public static void generateFile( String fileName, String[] input, int lines ) {

		StringBuilder sb = new StringBuilder();

		for ( int w = 0; w < lines; w++ ) {

			for ( int i = 0; i < input.length; i++ ) {
				sb.append( input[i] );
				sb.append( " " );
				sb.append( " \n" );
			}

		}

		File file = new File( fileName );
		try ( BufferedWriter writer = new BufferedWriter( new FileWriter( file ) ) ) {
			writer.write( sb.toString() );
		} catch ( IOException e ) {

			e.printStackTrace();
		}

	}
}
