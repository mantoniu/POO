package asdpoo.labtri;

/**
 * A class for simple sorting methods
 */
public class SimpleSorting {

	/**
	 * Sort the array in place using the selection sort algorithm
	 */
	public static <T extends Comparable<T>> void selection(T[] array) {

	}
	
	/**
	 * Sort the array in place using the insertion sort algorithm
	 */
	public static <T extends Comparable<T>> void insertion(T[] array) {

	}

	/**
	 * Sort the array in place using the bubble sort algorithm
	 */

	public static <T extends Comparable<T>> void bubble(T[] array) {
	}

	/**
	 * Swap array[i] and array[j]
	 */
	private static <T> void swap(T[] array, int i, int j) {
		T tmp = array[i];
		array[i] = array[j];
		array[j] = tmp;
	}
}
