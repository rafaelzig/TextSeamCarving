package tsinghua.algorithms.rafael;

/**
 * Class which is used to find and remove the minimum energy vertical seam in the input.
 *
 * @author Rafael da Silva Costa - 2015280364
 * @version 1.0
 */
class SeamFinder
{
	/**
	 * Uses seam carving to remove n columns from the input
	 *
	 * @param input Input to remove columns from
	 * @param n     number of columns to be removed
	 * @return Output that is n columns smaller than the input
	 */
	static int[][] carve(int[][] input, int n)
	{
		int[][] seams = calculateSeams(input);
		int[] minSeam = findMin(input, seams);
		int[][] output = removeSeam(input, minSeam);

		for (int i = 1; i < n; i++)
		{
			seams = removeSeam(seams, minSeam);
			recalculateSeams(output, seams, minSeam);
			minSeam = findMin(output, seams);
			output = removeSeam(output, minSeam);
		}

		return output;
	}

	/**
	 * Removes the smallest cost seam from the image data.
	 *
	 * @param input   Input data to be used during calculations
	 * @param minSeam Minimum vertical seam in the gradient data of the image
	 * @return Input data with the minimum cost seam removed.
	 */
	private static int[][] removeSeam(int[][] input, int[] minSeam)
	{
		int outputWidth = input[0].length - 1;
		int[][] output = new int[input.length][outputWidth];

		// Now fill in the new image one row at a time
		for (int row = 0; row < minSeam.length; row++)
		{
			int col = minSeam[row];

			if (col > 0)
			{
				// There are pixels to copy to the left of the minSeam
				System.arraycopy(input[row], 0, output[row], 0, col);
			}

			if (col < outputWidth)
			{
				// There are pixels to copy to the right of the minSeam
				int colsAfter = outputWidth - col;
				System.arraycopy(input[row], col + 1, output[row], col, colsAfter);
			}
		}

		return output;
	}

	/**
	 * Finds the minimum vertical seam in the gradient data of the image. A seam is a connected
	 * line through the image.
	 *
	 * @param input Input data to be used during calculations
	 * @param seams The cost of removing each cell in the input
	 * @return Array of the same height as the image where each entry specifies,
	 * for a single row, which column to remove.
	 */
	private static int[] findMin(int[][] input, int[][] seams)
	{
		int[] minSeam = new int[input.length];
		int lastRow = input.length - 1;

		// finds the column of the last row of the minimum seam
		int col = findEndPoint(seams);
		minSeam[(lastRow)] = col;

		// Starting from the last row of the DP table, the minSeam is found
		for (int row = lastRow; row > 0; row--)
		{
			minSeam[row] = col;
			col = findNextPoint(seams, row, col);
		}

		minSeam[0] = col;

		return minSeam;
	}

	/**
	 * Calculates the values of the dynamic programming table.
	 *
	 * @param input Input data to be used during calculations
	 */
	private static int[][] calculateSeams(int[][] input)
	{
		int height = input.length;
		int width = input[0].length;
		int[][] seams = new int[height][width];

		for (int row = 0; row < height; row++)
		{
			for (int col = 0; col < width; col++)
			{
				seams[row][col] = calculateCell(input, seams, row, col);
			}
		}

		return seams;
	}

	/**
	 * Recalculates the required values of the dynamic programming table from a previous iteration.
	 *
	 * @param input   Input data to be used during calculations
	 * @param seams   Dynamic programming table containing the costs to remove cells
	 * @param minSeam Minimum vertical seam in the input data
	 */
	private static void recalculateSeams(int[][] input, int[][] seams, int[] minSeam)
	{
		int height = input.length;
		int width = input[0].length;
		int begin;
		int end;

		if (minSeam[0] == 0)
		{
			begin = minSeam[0];
			end = minSeam[0];
		}
		else if (minSeam[0] == width)
		{
			begin = minSeam[0] - 1;
			end = minSeam[0] - 1;
		}
		else
		{
			begin = minSeam[0] - 1;
			end = minSeam[0];
		}

		for (int row = 1; row < height; row++)
		{
			for (int col = begin; col <= end; col++)
			{
				seams[row][col] = calculateCell(input, seams, row, col);
			}

			if (begin > 0)
			{
				begin--;
			}
			if (end < width - 1)
			{
				end++;
			}
		}
	}

	/**
	 * Calculates the minimum of the possible previous seam to fill in a cell of the current
	 * seam. Takes previous seam weight and then adds the value at seams[row][col].
	 *
	 * @param input Input data to be used during calculations
	 * @param seams Dynamic programming table containing the costs to remove cells
	 * @param row   Row to be examined
	 * @param col   Column to be examined
	 * @return Minimum cost of the possible previous seams
	 */
	private static int calculateCell(int[][] input, int[][] seams, int row, int col)
	{
		// initlizes first row
		if (row == 0)
		{
			return input[row][col];
		}

		// checks for single col input
		if (col == 0 && col == seams[0].length - 1)
		{
			return input[row][col] + seams[row - 1][col];
		}

		// if on the left edge does not consider going to the left
		if (col == 0)
		{
			return input[row][col] + Math.min(seams[row - 1][col], seams[row - 1][col + 1]);
		}

		// if on the right edge does not consider going to the right
		if (col == seams[0].length - 1)
		{
			return input[row][col] + Math.min(seams[row - 1][col], seams[row - 1][col - 1]);
		}

		// otherwise looks at the left, center, and right possibilities as mins
		return input[row][col] + Math.min(seams[row - 1][col - 1], Math.min(seams[row - 1][col], seams[row - 1][col + 1]));
	}

	/**
	 * Performs a linear search on the bottom row of table of seams to determine
	 * smallest seam.
	 *
	 * @param seams Dynamic programming table containing the costs to remove a cell
	 * @return The index of the smallest seam at the bottom row
	 */
	private static int findEndPoint(int[][] seams)
	{
		int lastRow = seams.length - 1;

		int minIndex = 0;
		int minValue = seams[lastRow][0];

		for (int col = 1; col < seams[0].length; col++)
		{
			if (seams[lastRow][col] < minValue)
			{
				minIndex = col;
				minValue = seams[lastRow][col];
			}
		}

		return minIndex;
	}

	/**
	 * From a certain (row,col) in the table seams, determines the previous smallest
	 * path and returns which column it is in.
	 *
	 * @param seams Dynamic programming table containing the costs to remove a cell
	 * @param row   Row to be examined
	 * @param col   Column to be examined
	 * @return Previous smallest column in the dynamic table of costs.
	 */
	private static int findNextPoint(int[][] seams, int row, int col)
	{
		int width = seams[row].length;

		// if at the left border only considers above and above to the right
		if (col == 0)
		{
			return (seams[row - 1][0] <= seams[row - 1][1]) ? 0 : 1;
		}

		// if at the right border only considers above and above to the left
		if (col == width - 1)
		{
			return (seams[row - 1][width - 2] <= seams[row - 1][width - 1]) ? width - 2 : width - 1;
		}

		// otherwise looks at above left, center, and right
		return checkAbove(seams, row, col);
	}

	/**
	 * Determines which column leads to smallest seam for a certain
	 * (row,col) and returns the column
	 *
	 * @param seams Dynamic programming table containing the costs to remove a cell
	 * @param row   Row to be examined
	 * @param col   Column to be examined
	 * @return Index of the smallest seam of the cells above.
	 */
	private static int checkAbove(int[][] seams, int row, int col)
	{
		int minIndex = col - 1;
		int minValue = seams[row - 1][col - 1];

		for (int i = 1; i < 3; i++)
		{
			if (seams[row - 1][col - 1 + i] < minValue)
			{
				minValue = seams[row - 1][col - 1 + i];
				minIndex = col - 1 + i;
			}
		}

		return minIndex;
	}
}