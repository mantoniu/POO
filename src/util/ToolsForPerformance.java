package util;

import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ToolsForPerformance {

    private static final Logger LOGGER = Logger.getLogger(ToolsForPerformance.class.getName());

    private ToolsForPerformance() {
    }

    /**
     * Evaluate the time of execution of a method in milliseconds
     *
     * @param runnable
     * @return the time of execution in milliseconds
     * It uses the Instant class to evaluate the time of execution
     */
    public static int evaluateTime(Runnable runnable) {
        Instant start = Instant.now();
        runnable.run();
        Instant end = Instant.now();
        Duration executionTime = Duration.between(start, end);
        return (int) executionTime.toMillis();
    }

    //to have a better evaluation of the time, we keep the nanoTime
    //private static final long NANO_TO_MILLI = 1; // 1000000; the good measure but for the test we use 1

    /**
     * Evaluate the time of execution of a method in nanoseconds
     * run the method times times
     * return the average time of execution in nanoseconds
     *
     * @param runnable
     * @param times
     * @return
     */
    public static long evaluateTime(Runnable runnable, int times) {
        if (times <= 0)
            throw new IllegalArgumentException("times must be greater than 0");
        long startTime = System.nanoTime();
        for (int i = 0; i < times; i++) {
            runnable.run();
        }
        long endTime = System.nanoTime();
        return (endTime - startTime) / times; // Durée moyenne en nanosecondes
    }



    /**
     * Record to store the min, max, average and  values
     *
     * @param minValue
     * @param maxValue
     * @param averageValue
     * @param standardDeviation
     * @param median
     */
    public record Measures(long minValue, long maxValue, double averageValue, double standardDeviation, long median) {
        public String toString() {
            return "Min : " + ToolsForPerformance.formatNumber(minValue) +
                    "\t Max : " + ToolsForPerformance.formatNumber(maxValue) +
                    "\t Average : " + ToolsForPerformance.formatNumber((long) averageValue) +
                    "\t Standard Deviation : " + ToolsForPerformance.formatNumber((long) standardDeviation) +
                    "\t Median : " + ToolsForPerformance.formatNumber(median);
        }
    }

    /**
     * Evaluate the time of execution of a method in nanoseconds
     * run the method times times
     * return the min, max and average time of execution in nanoseconds
     *
     * @param runnable
     * @param times
     * @return
     */
    public static Measures evaluateTimes(Runnable runnable, int times) {
        long[] durations = new long[times];

        if (times <= 0)
            throw new IllegalArgumentException("times must be greater than 0");
        //long startTime = System.nanoTime();
        for (int i = 0; i < times; i++) {
            long localStart = System.nanoTime();
            runnable.run();
            long localEnd = System.nanoTime();
            durations[i] = localEnd - localStart;
        }
        LOGGER.log(Level.INFO, () -> "durations : " + Arrays.toString(durations));
        //long endTime = System.nanoTime();
        //double average = Arrays.stream(durations).average().getAsDouble();
        long median = Arrays.stream(durations).sorted().skip(durations.length / 2).findFirst().getAsLong();
        double standardDeviation = calculateStandardDeviation(durations);
        Measures values = new Measures(
                Arrays.stream(durations).min().getAsLong(),
                Arrays.stream(durations).max().getAsLong(),
                Arrays.stream(durations).average().getAsDouble(),
                standardDeviation,
                median);
        LOGGER.log(Level.INFO, () -> runnable + " --> Measures :" + values);
        return values;
    }



    private static <T> Measures evaluateTimes(Consumer consumer, T[] array, int times) {
        long[] durations = new long[times];

        //Copy the array to keep the original array intact i.e. not sorted in case the consumer sorts the array in place
        T[] arrayCopy = Arrays.copyOf(array, array.length);

        if (times <= 0)
            throw new IllegalArgumentException("times must be greater than 0");
        //long startTime = System.nanoTime();
        for (int i = 0; i < times; i++) {
            long localStart = System.nanoTime();
            consumer.accept(arrayCopy);
            long localEnd = System.nanoTime();
            durations[i] = localEnd - localStart;
        }
        LOGGER.log(Level.INFO, () -> "durations : " + Arrays.toString(durations));
        //long endTime = System.nanoTime();
        //double average = Arrays.stream(durations).average().getAsDouble();
        long median = Arrays.stream(durations).sorted().skip(durations.length / 2).findFirst().getAsLong();
        double standardDeviation = calculateStandardDeviation(durations);
        Measures values = new Measures(
                Arrays.stream(durations).min().getAsLong(),
                Arrays.stream(durations).max().getAsLong(),
                Arrays.stream(durations).average().getAsDouble(),
                standardDeviation,
                median);
        LOGGER.log(Level.INFO, () -> consumer + " --> Measures :" + values);
        return values;
    }


    public record IdentifiedMeasure<T>(String identifier, T measure) {
        public String toString() {
            return identifier + " -> \t" + measure;
        }
    }

    /**
     * Calculate the standard deviation of an array
     *
     * @param array
     * @return the standard deviation
     * It uses the formula : sqrt(Σ(xi - mean)² / n)
     * where xi is the value of the array at index i
     * mean is the average of the array
     * n is the length of the array
     *
     */
    public static double calculateStandardDeviation(long[] array) {

        // get the sum of array
        double sum = 0.0;
        for (double i : array) {
            sum += i;
        }

        // get the mean of array
        int length = array.length;
        double mean = sum / length;

        // calculate the standard deviation
        double standardDeviation = 0.0;
        for (double num : array) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation / length);
    }

    public record IdentifiedMeasures(
            IdentifiedMeasure<Long> min,
            IdentifiedMeasure<Long> max,
            IdentifiedMeasure<Double> average,
            IdentifiedMeasure<Double> standardDeviation,
            IdentifiedMeasure<Long> median) {
        public String toString() {
            return "\n Min : " + min +
                    "\n Max : " + max +
                    "\n Average : " + average +
                    "\n Standard Deviation : " + standardDeviation +
                    "\n Median : " + median;
        }
    }

    /**
     * Compare a list of  Measures
     * and return the min identified measure for min, max and average values as IdentifiedMeasures
     */
    public static IdentifiedMeasures compareMeasures(List<IdentifiedMeasure<Measures>> measures) {
        //Compute the min and keep the identifier
        List<IdentifiedMeasure<Long>> minMeasures =
                measures.stream()
                        .map(m -> new IdentifiedMeasure<>(m.identifier(), m.measure().minValue()))
                        .toList();
        IdentifiedMeasure<Long> min = minMeasures.stream()
                .min(Comparator.comparing(IdentifiedMeasure::measure))
                .get();

        //Compute the max and keep the identifier
        List<IdentifiedMeasure<Long>> maxMeasures =
                measures.stream()
                        .map(m -> new IdentifiedMeasure<>(m.identifier(), m.measure().maxValue()))
                        .toList();
        IdentifiedMeasure<Long> max = maxMeasures.stream()
                .min(Comparator.comparing(IdentifiedMeasure::measure))
                .get();

        //Compute the average and keep the identifier
        List<IdentifiedMeasure<Double>> averageMeasures =
                measures.stream()
                        .map(m -> new IdentifiedMeasure<>(m.identifier(), m.measure().averageValue()))
                        .toList();
        IdentifiedMeasure<Double> average = averageMeasures.stream()
                .min(Comparator.comparing(IdentifiedMeasure::measure))
                .get();

        //Compute the standard deviation and keep the identifier
        List<IdentifiedMeasure<Double>> standardDeviationMeasures =
                measures.stream()
                        .map(m -> new IdentifiedMeasure<>(m.identifier(), m.measure().standardDeviation()))
                        .toList();
        IdentifiedMeasure<Double> standardDeviation = standardDeviationMeasures.stream()
                .min(Comparator.comparing(IdentifiedMeasure::measure))
                .get();

        //Compute the median and keep the identifier
        List<IdentifiedMeasure<Long>> medianMeasures =
                measures.stream()
                        .map(m -> new IdentifiedMeasure<>(m.identifier(), m.measure().median()))
                        .toList();
        IdentifiedMeasure<Long> median = medianMeasures.stream()
                .min(Comparator.comparing(IdentifiedMeasure::measure))
                .get();
        return new IdentifiedMeasures(min, max, average, standardDeviation, median);

    }

    /**
     * Compute the performances of a list of methods
     * and return a list of IdentifiedMeasures
     */
    public static List<IdentifiedMeasure<Measures>> evaluateMultiplePerformances(Map<String,Runnable> methods, int times) {
        return methods.entrySet().stream()
                .map(m -> new IdentifiedMeasure<>( m.getKey(),
                        evaluateTimes(m.getValue(), times)))
                .toList();
    }

    private static <T> List<IdentifiedMeasure<Measures>> evaluateMultiplePerformances(Map<String, Consumer<T[]>> methods, T[] array, int times) {
        return methods.entrySet().stream()
                .peek(e -> LOGGER.log(Level.INFO, () -> "===========> Method  evaluation start : " + e.getKey()))
                .map(m -> new IdentifiedMeasure<>( m.getKey(),
                        evaluateTimes(m.getValue(), array, times)))
                .toList()
                .stream().peek(e -> LOGGER.log(Level.INFO, () -> "===========| Method  evaluation end : " + e.identifier()))
                .toList();

    }


    /**
     * Compare the performances of a list of methods
     * and return the min, max and average values as IdentifiedMeasures
     */
    public static IdentifiedMeasures comparePerformances(Map<String,Runnable> methods, int times) {
        return compareMeasures(evaluateMultiplePerformances(methods, times));
    }

    public static <T> IdentifiedMeasures comparePerformances(Map<String, Consumer<T[]>> methods, T[] arg, int times) {
        return compareMeasures(evaluateMultiplePerformances(methods, arg, times));
    }



    /**
     * Evaluate Memory usage of a method
     * Difficult to evaluate the memory usage of a method because of the garbage collector
     * It's only usable when you are sure that the garbage collector doesn't free the memory
     */
    public static long evaluateMemory(Runnable runnable, int times) {
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        LOGGER.log(Level.INFO, () -> "Memory before: " + memoryBefore);
        for (int i = 0; i < times; i++) {
            runnable.run();
        }
        long memoryAfter = (runtime.totalMemory() - runtime.freeMemory());
        return (memoryAfter - memoryBefore) / times; // Average memory usage
    }

    /**
     * Only to show how to use the evaluateMemory method and the evaluateTime method
     *
     * @param args
     */
    public static void main(String[] args) {

        long memoryUsage = evaluateMemory(ToolsForPerformance::callCounter, 1000);
        LOGGER.log(Level.INFO, () -> "Average Memory usage: " + memoryUsage + " bytes");

        int time = (int) evaluateTime(() -> {
            long sum = 0; //Used to evaluate the time of execution I don't care about the result
            for (int i = 1; i <= 10000000; i++) {
                sum += i;
            }
        }, 1000);
        LOGGER.log(Level.INFO, () -> "Average Time: " + time + " ms");  // 0 ms

    }


    //This method is used to evaluate the memory usage of a method
    //We don't want the memory to be free by the garbage collector
    static List<Integer> counter = new ArrayList<>(100000);

    private static void callCounter() {
        for (int i = 0; i < 100000; i++) {
            counter.add(i);
        }
    }

    public record Couple(long oneTime, long secondTime) {
    }

    public static Couple comparePerformance(String one, Runnable r1, String two, Runnable r2) {
        long time1 = evaluateTime(r1, 10);
        long time2 = evaluateTime(r2, 10);
        LOGGER.log(Level.INFO, () -> "Time 1 : " + time1 + "ns" + " : " + one);
        LOGGER.log(Level.INFO, () -> "Time 2 : " + time2 + "ns" + " : " + two);
        if (time1 < time2) {
            LOGGER.log(Level.INFO, () -> one + " is faster than " + two);
        } else {
            LOGGER.log(Level.INFO, () -> two + " is faster than " + one);

        }
        return new Couple(time1, time2);
    }


    public static String formatNumber(Long duration) {
        NumberFormat numberFormat = NumberFormat.getInstance();
        // Indiquer que nous voulons le format avec des séparateurs de milliers
        numberFormat.setGroupingUsed(true);
        // Formater le nombre
        return numberFormat.format(duration);
    }
}
