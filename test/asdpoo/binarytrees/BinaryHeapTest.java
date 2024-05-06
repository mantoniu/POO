package asdpoo.binarytrees;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class BinaryHeapTest {

    Logger LOGGER = Logger.getLogger(BinaryHeapTest.class.getName());
    {
        LOGGER.setLevel(java.util.logging.Level.INFO);
    }
    BinaryHeap<Integer> binaryHeap;
    BinaryHeap<String> binaryHeapString;

    @BeforeEach
    void setUp() {
        binaryHeap = new BinaryHeap<Integer>(10, Comparator.naturalOrder());
        binaryHeapString = new BinaryHeap<>(new String[]{"A", "B", "C", "D", "E"}, Comparator.reverseOrder());
    }

    @Test
    void testHeapFromLesson() throws FullHeapException, EmptyHeapException {
        binaryHeap = new BinaryHeap<Integer>(new Integer[]{12, 5, 11, 3, 10, 2, 9, 4, 8, 1, 7, 6}, Comparator.reverseOrder());
        LOGGER.info(Arrays.toString(binaryHeap.getArray()));
        LOGGER.info(binaryHeap.byLevels());
        assertEquals(1, binaryHeap.extreme());
        assertEquals(12, binaryHeap.getArray()[7]);
        assertEquals(9, binaryHeap.getArray()[6]);
        assertTrue(binaryHeap.isLeaf(9));
        assertTrue(binaryHeap.isLeaf(10));
        assertTrue(binaryHeap.isLeaf(11));
        assertFalse(binaryHeap.isLeaf(6));
    }


    @Test
    void testSize() {
        assertEquals(0, binaryHeap.size());
        assertEquals(5, binaryHeapString.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(binaryHeap.isEmpty());
        assertFalse(binaryHeapString.isEmpty());
    }

    @Test
    void testExtreme() throws EmptyHeapException {
        assertThrows(EmptyHeapException.class, () -> binaryHeap.extreme());
        assertEquals("A", binaryHeapString.extreme());
        initHeap();
        assertEquals(9, binaryHeap.extreme());
        binaryHeapString = new BinaryHeap<>(new String[]{"A", "B", "C", "D", "E"}, Comparator.naturalOrder());
        assertEquals("E", binaryHeapString.extreme());
    }

    @Test
    void testConstructors() throws FullHeapException {
        assertArrayEquals(new String[]{"A", "B", "C", "D", "E"}, binaryHeapString.getArray());
    }
    @Test
    void testInsert() throws FullHeapException {
        binaryHeap.add(1);
        binaryHeap.add(5);
        binaryHeap.add(2);
        binaryHeap.add(10);
        binaryHeap.add(7);
        assertArrayEquals(new Integer[]{10, 7, 2, 1, 5, null, null, null, null, null}, binaryHeap.getArray());
    }

    @Test
    void testDelete() throws EmptyHeapException {
        LOGGER.info(binaryHeapString.byLevels());
        binaryHeapString.delete("C");
        testHeapEndIsFree(binaryHeap);
        binaryHeapString.delete("D");
        binaryHeapString.delete("E");
        testHeapEndIsFree(binaryHeap);
        assertArrayEquals(new String[]{"A", "B", null, null, null}, binaryHeapString.getArray());
    }

    @Test
    void testGetA() {
        assertArrayEquals(new Integer[]{null, null, null, null, null, null, null, null, null, null}, binaryHeap.getArray());
        assertArrayEquals(new String[]{"A", "B", "C", "D", "E"}, binaryHeapString.getArray());
    }

    public void initHeap() {
        binaryHeap = new BinaryHeap<>(3, 7, 5, 1, 9);
    }

    @Test
    void testDeleteExtreme() throws EmptyHeapException {
        initHeap();
        LOGGER.info("before delete" + Arrays.toString(binaryHeap.getArray()));
        LOGGER.info(binaryHeap.byLevels());
        assertEquals(Integer.valueOf(9), binaryHeap.deleteExtreme());
        testHeapEndIsFree(binaryHeap);
        assertEquals(Integer.valueOf(7), binaryHeap.deleteExtreme());
        testHeapEndIsFree(binaryHeap);
        assertEquals(Integer.valueOf(5), binaryHeap.deleteExtreme());
        testHeapEndIsFree(binaryHeap);
        assertEquals(Integer.valueOf(3), binaryHeap.deleteExtreme());
        testHeapEndIsFree(binaryHeap);
        assertEquals(Integer.valueOf(1), binaryHeap.deleteExtreme());
        testHeapEndIsFree(binaryHeap);
        assertTrue(binaryHeap.isEmpty());
    }

    private void testHeapEndIsFree(BinaryHeap binaryHeap) {
        LOGGER.info(binaryHeap.byLevels());
        LOGGER.info("insure the end of the table contains null : " + Arrays.toString(binaryHeap.getArray()));
        assertNull(binaryHeap.getArray()[binaryHeap.size()]);
    }

    @Test
    void testDeleteAll() throws FullHeapException {
        binaryHeap = new BinaryHeap<>(3, 7, 5, 1, 9,0,9,7,9);
        binaryHeap.deleteAll(9);
        testHeapEndIsFree(binaryHeap);
        assertArrayEquals(
                new Integer[]{7, 7, 5, 1, 3, 0, null, null,null}, binaryHeap.getArray());
    }

    @Test
    void testDeleteExtremeWithComparator() throws FullHeapException, EmptyHeapException {
        Comparator<Integer> c = (e1, e2) -> e2 - e1;
        BinaryHeap<Integer> heapWithComparator = new BinaryHeap<>(10, c);
        heapWithComparator.add(3);
        heapWithComparator.add(7);
        heapWithComparator.add(5);
        heapWithComparator.add(1);
        heapWithComparator.add(9);
        assertEquals(Integer.valueOf(1), heapWithComparator.deleteExtreme());
        assertEquals(Integer.valueOf(3), heapWithComparator.deleteExtreme());
        assertEquals(Integer.valueOf(5), heapWithComparator.deleteExtreme());
        assertEquals(Integer.valueOf(7), heapWithComparator.deleteExtreme());
        assertEquals(Integer.valueOf(9), heapWithComparator.deleteExtreme());
        assertTrue(heapWithComparator.isEmpty());
    }

}


