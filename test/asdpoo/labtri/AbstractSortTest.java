package asdpoo.labtri;

import util.ToolsForPerformance;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


abstract class AbstractSortTest {
    @Test
    void testGeneralCaseSort() {
        Integer[] array = { 5, 4, 3,9,10,15,14,13,12,2, 1,6,7,8,11,20, 19, 18, 17, 16};
        sort(array);
        assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5, 6, 7,8,9,10,11,12,13,14,15,16,17,18,19,20  }, array);
    }

    @Test
    void testEmptyArraySort() {
        Integer[] array = {};
        sort(array);
        assertArrayEquals(new Integer[] {}, array);
    }

    @Test
    void testAlreadySortedArraySort() {
        Integer[] array = { 1, 2, 3, 4, 5 };
        sort(array);
        assertArrayEquals(new Integer[] { 1, 2, 3, 4, 5 }, array);
    }


    @Test
    void testArrayWithRepeatedElementsSort() {
        Integer[] array = { 1, 2, 3, 4, 5, 5, 4, 3, 2, 1 };
        sort(array);
        assertArrayEquals(new Integer[] { 1, 1, 2, 2, 3, 3, 4, 4, 5, 5 }, array);
    }

    @Test
    void testArrayWithNegativeElementsSort() {
        Integer[] array = { -1, -2, -3, -4, -5, -5, -4, -3, -2, -1 };
        sort(array);
        assertArrayEquals(new Integer[] { -5, -5, -4, -4, -3, -3, -2, -2, -1, -1 }, array);
    }

    final double NanoToMilli = 1000000;
    final int BigTreeSize = 10000;
    //@todo : utilise les méthodes de util.
    @Test
    void evaluatePerformances() {
        Integer[] smallArray = { 9, 1, 4, 8, 2, 1, 3, 7, 5, 6 };
        long duration = ToolsForPerformance.evaluateTime(() -> sort(Arrays.copyOf(smallArray,smallArray.length)), 100);
        System.out.println("Duration for small array: " + ToolsForPerformance.formatNumber(duration) + " ns");
        System.out.println("Soit: " + duration/NanoToMilli + " ms");
        sort(smallArray);

        System.out.println("sorted array: " + Arrays.toString(smallArray));
        duration = ToolsForPerformance.evaluateTime(() -> sort(Arrays.copyOf(smallArray,smallArray.length)), 100);
        System.out.println("Duration for sorted small array: " + ToolsForPerformance.formatNumber(duration) + " ns");

        Arrays.sort(smallArray, Comparator.reverseOrder());
        System.out.println("reversed array: " + Arrays.toString(smallArray));
        duration = ToolsForPerformance.evaluateTime(() -> sort(Arrays.copyOf(smallArray, smallArray.length)), 100);
        System.out.println("Duration for reverse sorted small array: " + ToolsForPerformance.formatNumber(duration) + " ns");

        System.out.println("\nPerformances for big array");
        Integer[] array = buildBigTree(BigTreeSize);
        duration = ToolsForPerformance.evaluateTime(() -> sort(Arrays.copyOf(array, array.length)), 10);
        System.out.println("Duration for big array : " + ToolsForPerformance.formatNumber(duration) + " ns");
        System.out.println("Soit: " + duration/NanoToMilli + " ms");
        sort(array);
        System.out.println("sorted big array" + "[ " + array[0] + " , " + array[1] + " , ... " +  array[array.length - 1] + " ]");
        duration = ToolsForPerformance.evaluateTime(() -> sort(Arrays.copyOf(array, array.length)), 10);
        System.out.println("Duration for sorted big array : " + ToolsForPerformance.formatNumber(duration) + " ns");
        System.out.println("Soit: " + duration/NanoToMilli + " ms");

        Arrays.sort(array, Comparator.reverseOrder());
        System.out.println("reversed big array" + "[ " + array[0] + " , " + array[1] + " , ... " +  array[array.length - 1] + " ]");
        duration = ToolsForPerformance.evaluateTime(() -> sort(Arrays.copyOf(array, array.length)), 10);
        System.out.println("Duration for reversed sorted big array : " + ToolsForPerformance.formatNumber(duration) + " ns");
        System.out.println("Soit: " + duration/NanoToMilli + " ms");

       /* long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++){
            sort(Arrays.copyOf(array, array.length));
        }
        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start) + "ms");

        */
    }

    @Test
    void evaluateDetailedPerformance() {
        Integer[] array = buildBigTree(BigTreeSize);
        ToolsForPerformance.Measures measures = ToolsForPerformance.evaluateTimes(() -> sort(Arrays.copyOf(array, array.length)), 10);
        System.out.println("Measures: \n " + measures);
        System.out.println("Soit en moyenne: " + measures.averageValue()/NanoToMilli + " ms");
       /* long start = System.currentTimeMillis();
        for (int i = 0; i < 100; i++){
            sort(Arrays.copyOf(array, array.length));
        }
        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start) + "ms");

        */
    }

    private static Integer[] buildBigTree(int size) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * size);
        }
        return array;
    }


    // Abstract method to be implemented in subclasses
    protected abstract void sort(Integer[] array);



}