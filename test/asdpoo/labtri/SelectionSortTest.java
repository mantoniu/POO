package asdpoo.labtri;

class SelectionSortTest extends AbstractSortTest{

    @Override
    protected void sort(Integer[] array) {
        SimpleSorting.selection(array);
    }


}
