package asdpoo.labtri;

class InsertionSortTest extends AbstractSortTest{

    @Override
    protected void sort(Integer[] array) {
        SimpleSorting.insertion(array);
    }


}
