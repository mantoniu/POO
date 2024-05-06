package util;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

class ToolsForPerformanceTest {


    static List<Integer> counter = new ArrayList<>(100000);
    static void callCounter() {
        for (int i = 0; i < 100000; i++) {
            counter.add(i);
        }
    }

    static List<Integer> fillRandomList(int size) {
        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add((int) (Math.random() * size));
        }
        return list;
    }


    @Test
    void testEvaluateTime() {
        long duration = ToolsForPerformance.evaluateTime(ToolsForPerformanceTest::callCounter, 10);
        System.out.println("Duration: " + ToolsForPerformance.formatNumber(duration) + " ns");
        assertTrue(duration > 800000);
    }


    @Test
    void testEvaluateTimes() {
        ToolsForPerformance.Measures measures = ToolsForPerformance.evaluateTimes(ToolsForPerformanceTest::callCounter, 10);
        System.out.println("Measures: " +  measures);
        assertTrue(measures.minValue() <300000);
        assertTrue(measures.maxValue() > measures.averageValue()
                && measures.minValue() < measures.averageValue());
/*
        ToolsForPerformance.Measures measures4Counter = ToolsForPerformance.evaluateTimes(ToolsForPerformanceTest::callCounter, 10);
        Logger.getGlobal().info("Measures for filling counter: " +  measures4Counter);
        AtomicReference<List<Integer>> list = new AtomicReference<>();
        Runnable filling = () -> {
            list.set(fillRandomList(100000));
        };
        ToolsForPerformance.Measures measures4list = ToolsForPerformance.evaluateTimes(filling, 10);
        Logger.getGlobal().info("Measures for filling list : " +  measures4list);
        Logger.getGlobal().info("List size: " + list.get().size());
        ToolsForPerformance.Measures measures4Sort = ToolsForPerformance.evaluateTimes(() -> list.get().sort(Comparator.naturalOrder()), 10);
        Logger.getGlobal().info("Measures for sorting list : " +  measures4Sort);
*/

    }

    @Test
    void testEvaluateMultiplePerformances() {
        Map<String, Runnable> map = getStringRunnableMap();
        List<ToolsForPerformance.IdentifiedMeasure<ToolsForPerformance.Measures>> measures =
                ToolsForPerformance.evaluateMultiplePerformances(map, 10);
        System.out.println("Measures: \n" +  measures);
        assertEquals(3, measures.size());
        assertEquals(map.keySet().stream().sorted().toList(),
                measures.stream().map(ToolsForPerformance.IdentifiedMeasure::identifier).toList().stream().sorted().toList());


    }

    private static Map<String, Runnable> getStringRunnableMap() {
        AtomicReference<List<Integer>> list = new AtomicReference<>();
        list.set(fillRandomList(100000));
        Runnable filling = () -> {
            list.set(fillRandomList(100000));
        };

        Runnable callCounter = ToolsForPerformanceTest::callCounter;
        Runnable sort = () -> list.get().sort(Comparator.naturalOrder());

        Map<String, Runnable> map = new HashMap<>();
        map.put("callCounter", callCounter);
        map.put("filling", filling);
        map.put("sort", sort);
        return map;
    }

    @Test
    void testComparePerformances() {
        Map<String, Runnable> map = getStringRunnableMap();
        ToolsForPerformance.IdentifiedMeasures identifiedMeasures = ToolsForPerformance.comparePerformances(map, 10);
        System.out.println("IdentifiedMeasures: \n" + identifiedMeasures);
    }

}