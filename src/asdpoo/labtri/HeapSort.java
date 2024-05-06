package asdpoo.labtri;


import asdpoo.binarytrees.BinaryHeap;

/**
 * A class for the heap sort algorithm.
 */
public class HeapSort {
	
	/**
	 * Sort the array in place using the heapsort algorithm
	 * Complexity: THETA( n.log(n) ) where n is the size of the array
	 */	
	public static <T extends Comparable<T>> void sort(T[] array) {
		BinaryHeap<T> heap = new BinaryHeap<>(array);

	}


}
