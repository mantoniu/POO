package asdpoo.labtri;

import asdpoo.binarytrees.BinaryHeap;
import org.junit.jupiter.api.Test;
import util.ToolsForPerformance;

import java.util.Arrays;

class HeapSortTest extends AbstractSortTest{

    @Override
    protected void sort(Integer[] array) {
        HeapSort.sort(array);
    }

/*    @Test
    void testPerformanceWithoutBuildingHeap() {
        Integer[] array = new Integer[100000];
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * 100000);
        }
        BinaryHeap<Integer> heap = new BinaryHeap<>(array);
        long duration = ToolsForPerformance.evaluateTime(() -> HeapSort.onlyExtract(Arrays.copyOf(array, array.length),heap), 10);
        System.out.println("Duration: " + ToolsForPerformance.formatNumber(duration) + "ns");
        System.out.println("Soit: " + duration/1000000 + "ms");
    }

 */
}
