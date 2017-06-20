package tsinghua.algorithms.rafael;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.AbstractMap;
import java.util.regex.Pattern;

/**
 * Utilities class for parsing purposes.
 *
 * @author Rafael da Silva Costa - 2015280364
 * @version 1.0
 */
class Utils
{
	// Pattern object used during parsing
	private static final Pattern PATTERN = Pattern.compile(" ");

	/**
	 * Reads and parses the contents of the input file at the provided path.
	 *
	 * @param path Path of the file to be processed.
	 * @return A pair of values containing the number of seams to be removed and the input data
	 */
	static AbstractMap.SimpleEntry<Integer, int[][]> readInput(String path)
	{
		int[][] input = null;
		int n = -1;

		try (BufferedReader in = Files.newBufferedReader(new File(path).toPath()))
		{
			String[] params = PATTERN.split(in.readLine());
			input = parseTable(in, Integer.parseInt(params[0]), Integer.parseInt(params[1]));
			n = Integer.parseInt(params[2]);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return new AbstractMap.SimpleEntry<>(n, input);
	}

	/**
	 * Parses the input contained in the provided BufferedReader object, returning the input data contained in it.
	 *
	 * @param in     BufferedReader object containing the input data to be read.
	 * @param height Height of the input data
	 * @param width  Width of the input data
	 * @return Parsed input data in the form of a 2D array
	 * @throws IOException Signals that an I/O exception of some sort has occurred.
	 */
	private static int[][] parseTable(BufferedReader in, int height, int width) throws IOException
	{
		int[][] input = new int[height][width];

		for (int row = 0; row < height; row++)
		{
			String[] values = PATTERN.split(in.readLine());

			for (int col = 0; col < width; col++)
			{
				input[row][col] = Integer.parseInt(values[col]);
			}
		}

		return input;
	}

	/**
	 * Reads and parses the contents of the output file at the provided path.
	 *
	 * @param path   Path of the file to be processed.
	 * @param height Height of the input data
	 * @param width  Width of the input data
	 * @return Parsed output data in the form of a 2D array
	 */
	static int[][] readOutput(String path, int height, int width)
	{
		int[][] output = null;

		try (BufferedReader in = Files.newBufferedReader(new File(path).toPath()))
		{
			output = parseTable(in, height, width);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return output;
	}

	/**
	 * Compares both outputs and returns true if they are equal, false otherwise.
	 *
	 * @param actual   Output produced by the application
	 * @param expected Output provided by the user
	 * @return true if the outputs are equal, false otherwise.
	 */
	static boolean checkCorrectness(int[][] actual, int[][] expected)
	{
		if (actual.length != expected.length || actual[0].length != expected[0].length)
		{
			return false;
		}

		for (int row = 0; row < actual.length; row++)
		{
			for (int col = 0; col < actual[row].length; col++)
			{
				if (actual[row][col] != expected[row][col])
				{
					return false;
				}
			}
		}

		return true;
	}
}