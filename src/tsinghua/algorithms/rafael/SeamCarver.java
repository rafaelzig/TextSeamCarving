package tsinghua.algorithms.rafael;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Random;

/**
 * Class which removes seams from a text file specified in the arguments of the command line.
 *
 * @author Rafael da Silva Costa - 2015280364
 * @version 1.0
 */
class SeamCarver
{
	/**
	 * @param args Command line arguments in the form: "input.txt output.txt" - where input.txt is the input
	 *             file and output.txt is expected output to be compared with the actual output of this application
	 */
	public static void main(String[] args)
	{
		AbstractMap.SimpleEntry<Integer, int[][]> pair = Utils.readInput(args[0]);
		int[][] input = pair.getValue();
		int n = pair.getKey();

//		int[][] input = computeEnergyMatrix(10, 10, 10);
//		int n = 9;

		System.out.println("Input:");
		Arrays.stream(input).forEach(a -> System.out.println(Arrays.toString(a)));

		long start = System.currentTimeMillis();
		int[][] output = remove(input, n);
		long elapsed = System.currentTimeMillis() - start;

		System.out.println("Output:");
		Arrays.stream(output).forEach(a -> System.out.println(Arrays.toString(a)));

		System.out.println("Calculated in " + elapsed + "ms");

		if (Utils.checkCorrectness(output, Utils.readOutput(args[1], input.length, input[0].length - n)))
		{
			System.out.println("Correct!");
		}
		else
		{
			System.out.println("Incorrect!");
		}
	}

	/**
	 * Uses seam carving to remove n columns from the input
	 *
	 * @param input Input data to remove columns
	 * @param n     number of columns to be removed
	 * @return Output with n columns smaller than the input
	 * @throws IllegalArgumentException if the input is invalid or the specified value of n is greater than the input width
	 */
	static int[][] remove(int[][] input, int n)
			throws IllegalArgumentException
	{
		if (input[0].length - 1 < 0)
		{
			throw new IllegalArgumentException("The input must be at least one column wide");
		}
		else if (n >= input[0].length)
		{
			throw new IllegalArgumentException("The number of columns to be removed cannot be greater than the number of the input width");
		}

		return SeamFinder.carve(input, n);
	}

	/**
	 * Debuggins method used to generate random inputs.
	 *
	 * @param height    Height of the input to be generated.
	 * @param width     Width of the input to be generated.
	 * @param maxEnergy Max value for each cell in the input.
	 * @return Generated input.
	 */
	private static int[][] computeEnergyMatrix(int height, int width, int maxEnergy)
	{
		Random random = new Random();
		int[][] input = new int[height][width];

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				input[i][j] = random.nextInt(maxEnergy) + 1;
			}
		}

		return input;
	}
}