package asdpoo.labtri;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class for the quicksort algorithm
 */
public class QuickSort {



	private static final int CUTOFF = 10;
	private static final Logger LOGGER = Logger.getLogger(QuickSort.class.getName());

	static {
		LOGGER.setLevel(Level.SEVERE);
	}

	/**
	 * Sort the array in place using the quicksort algorithm
	 */
	public static <T extends Comparable<T>> void sort(T[] array) {
		sort(array,0,array.length-1);
	}

	/**
	 * Sort the portion array[lo,hi] in place using the quicksort algorithm
	 */	
	private static <T extends Comparable<T>> void sort(T[] array, int lo, int hi) {

	}

	/**
	 * Partition the portion array[lo,hi] and return the index of the pivot
	 * We use a logger to trace the execution of the algorithm and help the student to understand the algorithm
	 */
	private static <T extends Comparable<T>> int partition(T[] array, int lo, int hi) {
		return 0;
	}

	/**
	 * Return the index of the median of { array[lo], array[mid], array[hi] }
	 *
	 */
	private static <T extends Comparable<T>> int median(T[] array, int lo, int mid, int hi) {
		return 0;
	}

	
	/**
	 * Sort array[lo, hi] in place using the insertion sort algorithm
	 */
	private static <T extends Comparable<T>> void insertion(T[] array, int lo, int hi) {
	}
	
	/**
	 * Swap array[i] and array[j]
	 */
	private static <T> void swap(T[] array, int i, int j) {
		if ( i == j )
			return;
		T tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}
}
