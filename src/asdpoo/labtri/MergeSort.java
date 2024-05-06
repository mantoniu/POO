package asdpoo.labtri;

/**
 * A class for the recursive merge sort algorithm.
 */
public class MergeSort {

	/**
	 * Sort the array using the recursive merge sort algorithm.
	 * This algorithm is not in place and needs an auxiliary array of
	 * the same size as the array to sort
	 * Complexity: THETA( n.log(n) ) where n is the size of the array
	 */

	public static <T extends Comparable<T>> void sort(T[] array) {
		T[] tmp = (T[]) new Comparable[array.length];
		sort(array, tmp, 0, array.length - 1);
	}


	/**
	 * Sort the array in the range [low, high] using the portion [low, high]
	 * of the auxiliary array tmp
	 * Complexity: THETA( n.log(n) ) where n = high - low + 1
	 */
	private static <T extends Comparable<T>> void sort(T[] array, T[] tmp, int low, int high) {

	}


	/**
	 * Merge array[low, mid] and array[mid+1, high] into tmp[low, high]
	 * Precondition: array[low, mid] and array[mid+1, high] are sorted
	 * Complexity: THETA( n ) where n = high - low + 1
	 */
	private static <T extends Comparable<T>> void merge(T[] array, T[] tmp, int low, int mid, int high) {

	}

	/**
	 * Copy the elements from tmp[lo, hi] into array[lo, hi]
	 * Complexity: THETA( n ) where n = hi - lo + 1
	 */
	private static <T> void transfer(T[] tmp, T[] array, int lo, int hi) {
	}


}