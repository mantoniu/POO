package asdpoo.labtri;

class MergeSortTest extends AbstractSortTest{

    @Override
    protected void sort(Integer[] array) {
        MergeSort.sort(array);
    }
}
