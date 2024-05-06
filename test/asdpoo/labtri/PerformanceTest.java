package asdpoo.labtri;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import util.ToolsForPerformance;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.*;
import java.util.logging.Formatter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class PerformanceTest {

    record Couple(String name, Consumer<Integer[]> sort) {
    }



    static Stream<Couple> functions(){
        return Stream.of(
                new Couple ("Arrays.sort", (Integer[] array) -> Arrays.sort(array)),
                new Couple ("selection", (Integer[] array) -> SimpleSorting.selection(array)),
                new Couple ("insertion", (Integer[] array) -> SimpleSorting.insertion(array)),
                //heap sort includes to build the heap and then sort it... too bad
                new Couple ("heapsort", (Integer[] array) ->  HeapSort.sort(array)),
                new Couple ("quicksort", (Integer[] array) -> QuickSort.sort(array)),
                new Couple ("mergesort", (Integer[] array) -> MergeSort.sort(array))
        );
    }



    private <T> ToolsForPerformance.Measures evaluateDurationAndDisplay(Integer[] array, Couple couple) {
        ToolsForPerformance.Measures measures = ToolsForPerformance.evaluateTimes(() -> couple.sort.accept(Arrays.copyOf(array, array.length)), 10);
        System.out.println(couple.name + " - Duration: " + measures);
        return measures;
    }
     /*   long duration = ToolsForPerformance.evaluateTime(() -> couple.sort.accept(Arrays.copyOf(array, array.length)), 10);
        NumberFormat numberFormat = NumberFormat.getInstance();
        // Indiquer que nous voulons le format avec des séparateurs de milliers
        numberFormat.setGroupingUsed(true);

        // Formater le nombre
        String formattedNumber = numberFormat.format(duration);
        System.out.println(couple.name + " - Duration: " + formattedNumber + " ns");

    }
      */

    private static Integer[] buildBigArray() {
        Integer[] array = new Integer[100000];
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * 100000);
        }
        return array;
    }

    //To avoid some drawbacks due to different big trees
    Integer[] bigArray = buildBigArray();
    @ParameterizedTest
    @MethodSource("functions")
    void testCompareDurationOnBigTree( Couple sort) {
        Integer[] array = Arrays.copyOf(this.bigArray, this.bigArray.length);
        ToolsForPerformance.Measures res = evaluateDurationAndDisplay(array, sort);
        assertTrue(res.minValue() < res.averageValue());
        assertTrue(res.maxValue() > res.averageValue());

    }



    @ParameterizedTest
    @MethodSource("functions")
    void testCompareDurationOnSmallTree(Couple sort) {
        Integer[] array = { 5, 4, 3, 2, 1 };
        evaluateDurationAndDisplay(array, sort);

    }



    @ParameterizedTest
    @MethodSource("functions")
    void testCompareDurationOnEmptyTree(Couple sort) {
        Integer[] array = {};
        evaluateDurationAndDisplay(array, sort);
    }

    @ParameterizedTest
    @MethodSource("functions")
    void testCompareAlreadySortedArraySort(Couple sort) {
        Integer[] array = { 1, 2, 3, 4, 5 };
        evaluateDurationAndDisplay(array, sort);
    }

    @ParameterizedTest
    @MethodSource("functions")
    void testCompareArrayWithRepeatedElementsSort(Couple sort) {
        Integer[] array = { 1, 2, 3, 4, 5, 5, 4, 3, 2, 1 };
        evaluateDurationAndDisplay(array, sort);
    }

    @ParameterizedTest
    @MethodSource("functions")
    void testCompareArrayWithNegativeElementsSort(Couple sort) {
        Integer[] array = { -1, -2, -3, -4, -5, -5, -4, -3, -2, -1 };
        evaluateDurationAndDisplay(array, sort);
    }

    int sizeOfTestArray = 1000000;
    @Test
    void testComparePerformance() {
        Integer[] array = new Integer[sizeOfTestArray];
        for (int i = 0; i < array.length; i++) {
            array[i] = (int) (Math.random() * sizeOfTestArray);
        }
        Map<String, Consumer<Integer[]>> map = new HashMap<>();
        //Too good, we have to remove it from the comparison
       //map.put("Arrays.sort", (Integer[] a) -> Arrays.sort(a));
        //too long, we have to remove it
        //map.put("selection", (Integer[] a) -> SimpleSorting.selection(a));
        //too long, we have to remove it
        //map.put("insertion", (Integer[] a) -> SimpleSorting.insertion(a));
        map.put("heapsort", (Integer[] a) -> HeapSort.sort(a));
        map.put("quicksort", (Integer[] a) -> QuickSort.sort(a));
        map.put("mergesort", (Integer[] a) -> MergeSort.sort(a));
        ToolsForPerformance.IdentifiedMeasures measures = ToolsForPerformance.comparePerformances(map, array, 10);
        //List<ToolsForPerformance.IdentifiedMeasure<ToolsForPerformance.Measures>> measures = ToolsForPerformance.evaluateMultiplePerformances(map, 10);
        System.out.println("Measures: \n" +  measures);
        //assertTrue(measures.size() == 6);
    }


    /*   --------------------------------------------------
                  Extracted from last Year
    ------------------------------------------------------- */
        private static final Logger LOGGER = Logger.getLogger(PerformanceTest.class.getName());

        static {
            LOGGER.setLevel(Level.SEVERE);

            Handler handler = new ConsoleHandler();
            Formatter formatter = new Formatter() {
                public String format(LogRecord record) {
                    return record.getMessage() + System.lineSeparator();
                }
            };
            handler.setLevel(Level.INFO);

            // Affecter le formatter personnalisé au handler
            handler.setFormatter(formatter);

            // Ajouter le handler au logger
            LOGGER.addHandler(handler);
            LOGGER.info("Logger initialized : " + LOGGER.getName());
        }

        record MinFor(long duration, String identifier) {}

        private static MinFor minFor(Long[] durations, String[] identifiers) {
            if (durations.length != identifiers.length) {
                throw new IllegalArgumentException("The two arrays must have the same length" + Arrays.toString(durations) + " : " + Arrays.toString(identifiers) );
            }
            LOGGER.info(() -> "durations : " + Arrays.toString(durations) + " for  identifiers : " + Arrays.toString(identifiers));

            long min = Long.MAX_VALUE;
            String minIdentifier = "";

            for (int i = 0; i < durations.length; i++) {
                if (durations[i] < min) {
                    min = durations[i];
                    minIdentifier = identifiers[i];
                }
            }
            return new MinFor(min, minIdentifier);
        }
        private static void logMessage(Long[] durations, String[] identifiers) {
            if (LOGGER.isLoggable(java.util.logging.Level.INFO)) {
                MinFor minIdent = minFor(durations, identifiers);
                LOGGER.info("The best one is " + minIdent.identifier() + " with a duration of " + minIdent.duration() + " ms");
            }
        }


        public static <T extends Comparable<T>> void sortByJava(T[] array) {
            Arrays.sort(array);
        }

        public static final int NUMBER_OF_ITERATIONS = 10;
        public static <T> long timeFor(Consumer<T[]> sortFunction, T[] array, String message) {
            long debut = System.nanoTime();
            for (int i = 0; i < NUMBER_OF_ITERATIONS; i++){
                sortFunction.accept(array);
            }
            long fin = System.nanoTime();
            long duree = fin - debut;
            LOGGER.fine(() -> message+ " : runtime for " + NUMBER_OF_ITERATIONS + " iterations : " + duree + " ns" );
            return duree;
        }

        private static final Random RANDOM = new Random();

        private static Integer[]  generateRandomArray(int size) {
            Integer[] array = new Integer[size];
            for (int i = 0; i < size; i++) {
                array[i] = RANDOM.nextInt(size);
            }
            return array;
        }

        private static Integer[] generateSortedArray(int size) {
            Integer[] sortedArray = new Integer[size];
            for (int j = 0; j < size; j++) {
                sortedArray[j] = j;
            }
            return sortedArray;
        }


        record DurationFor(Consumer<Integer[]> consumer, long durationForSortedArray, long durationForReverseSortedArray, long durationForRandomArray) {}


        private static Integer[] copy(Integer[] array){
            return Arrays.copyOf(array, array.length);
        }

        //Quick sort fails on 1000000 elements the stack is too deep
        public static void main(String[] args)  {
            LOGGER.setLevel(Level.INFO);

            Integer[] sortedArray = generateSortedArray(10000);
            Integer[]  reverseSortedArray = Arrays.stream(sortedArray).sorted(Comparator.reverseOrder()).toArray(Integer[]::new);
            Integer[] randomArray = generateRandomArray(100000);

            String[] algoToTest  = new String[]{
                    "MergeSort::sort",
                    "QuickSort::sort",
                    "ForRuntimePerformanceTesting::sortByJava",
                    "SimpleSorting::selection",
                    "SimpleSorting::insertion",
                    "HeapSort::sort"};

            //initialize the array of consumers
            Consumer<Integer[]> [] algoToTestConsumer = new Consumer[algoToTest.length];
            algoToTestConsumer[0] = MergeSort::sort;
            algoToTestConsumer[1] = QuickSort::sort;
            algoToTestConsumer[2] = Arrays::sort;
            algoToTestConsumer[3] = SimpleSorting::selection;
            algoToTestConsumer[4] = SimpleSorting::insertion;
            algoToTestConsumer[5] = HeapSort::sort;

            //initialize the map of averages
            Map<String, DurationFor> averages = new HashMap<>();
            int i = 0;
            for(String eachAlgo : algoToTest) {
                averages.put(eachAlgo, new DurationFor(algoToTestConsumer[i],0, 0, 0));
                i++;
            }
      /*  averages.put("MergeSort::sort", new DurationFor(algoToTestConsumer[0],0, 0, 0));
        averages.put("QuickSort::sort", new DurationFor(algoToTestConsumer[1],0, 0, 0));
        averages.put("ForRuntimePerformanceTesting::sortByJava", new DurationFor(algoToTestConsumer[2],0, 0, 0));
        averages.put("SimpleSorting::selection", new DurationFor(algoToTestConsumer[3],0, 0, 0));
        averages.put("SimpleSorting::insertion", new DurationFor(algoToTestConsumer[4],0, 0, 0));
        averages.put("HeapSort::sort", new DurationFor(algoToTestConsumer[5],0, 0, 0));
      */
            //for (int i = 0; i < 10; i++) {
            for(Map.Entry<String, DurationFor> eachAlgoEntry : averages.entrySet()) {
                LOGGER.info(">>>> Testing " + eachAlgoEntry.getKey());
                //sorted array
                long localtime = timeFor(eachAlgoEntry.getValue().consumer(), copy(sortedArray), eachAlgoEntry.getKey() + " on sorted array: ");
                long recordedValue = eachAlgoEntry.getValue().durationForSortedArray();
                eachAlgoEntry.setValue(new DurationFor(eachAlgoEntry.getValue().consumer(), recordedValue + localtime, eachAlgoEntry.getValue().durationForReverseSortedArray(), eachAlgoEntry.getValue().durationForRandomArray()));
                LOGGER.fine("Average for " + eachAlgoEntry.getKey() + " on sorted array: " + eachAlgoEntry.getValue().durationForSortedArray()/10);

                //reverse sorted array
                localtime = timeFor(eachAlgoEntry.getValue().consumer(), copy(reverseSortedArray), eachAlgoEntry.getKey() + " on reverse sorted array: ");
                recordedValue = eachAlgoEntry.getValue().durationForReverseSortedArray();
                eachAlgoEntry.setValue(new DurationFor(eachAlgoEntry.getValue().consumer(), eachAlgoEntry.getValue().durationForSortedArray(), recordedValue + localtime, eachAlgoEntry.getValue().durationForRandomArray()));
                LOGGER.fine("Average for " + eachAlgoEntry.getKey() + " on reverse sorted array: " + eachAlgoEntry.getValue().durationForReverseSortedArray()/10);

                //random array
                localtime = timeFor(eachAlgoEntry.getValue().consumer(), copy(randomArray), eachAlgoEntry.getKey() + " on random array: ");
                recordedValue = eachAlgoEntry.getValue().durationForRandomArray();
                eachAlgoEntry.setValue(new DurationFor(eachAlgoEntry.getValue().consumer(), eachAlgoEntry.getValue().durationForSortedArray(), eachAlgoEntry.getValue().durationForReverseSortedArray(), recordedValue + localtime));
                LOGGER.fine("Average for " + eachAlgoEntry.getKey() + " on random array: " + eachAlgoEntry.getValue().durationForRandomArray()/10);

            }
            //  }

            List<Long> durations = new ArrayList<>();
            for (String algo : algoToTest){
                durations.add(averages.get(algo).durationForSortedArray());
            }
            MinFor minForSortedArray = minFor(durations.toArray(new Long[0]), algoToTest);
            durations.clear();
            for (String algo : algoToTest){
                durations.add(averages.get(algo).durationForReverseSortedArray());
            }
            MinFor minForReverseSortedArray = minFor(durations.toArray(new Long[0]), algoToTest);
            durations.clear();

            for (String algo : algoToTest){
                durations.add(averages.get(algo).durationForRandomArray());
            }
            MinFor minForRandomArray = minFor((Long[])durations.toArray(new Long[0]), algoToTest);
            durations.clear();


            System.out.println("min for sorted array: " + minForSortedArray);
            System.out.println("min for reverse sorted array: " + minForReverseSortedArray);
            System.out.println("min for random array: " + minForRandomArray);

        }



    }



