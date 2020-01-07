package com.jfriedly.interviewing.practice.tadm2e.cycledetection;

import com.jfriedly.interviewing.practice.generic.ListNode;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Random;

public class CycleDetectorTest {

    private static final Logger logger = LoggerFactory.getLogger(CycleDetectorTest.class);
    private static final Random RANDOM = new Random();

    /**
     * Returns pairs of integers that can be interpreted as the number of nodes before a cycle and the number of nodes
     * on the cycle.  Literature refers to the first value as *mu* and the second as *lambda*.  Zero nodes before the
     * cycle indicates that the entire linked list should be a cycle (i.e. a circular list), zero nodes on the cycle
     * indicates that there should not actually be a cycle, and both values as zero indicates that the list is simply
     * null.  Negative values are not allowed.
     *
     * Note that, with zero-indexing, the index of the first node on the cycle is equal to the number of nodes before
     * the cycle.
     */
    @DataProvider
    public Object[][] staticInputProvider() {
        return new Object[][] {
                // Null linked list
                {0, 0},
                // No cycle linked lists
                {1, 0},
                {2, 0},
                {3, 0},
                // No nodes before the cycle starts, i.e. the list is circular
                {0, 1},
                {0, 2},
                {0, 3},
                // A single node before the cycle begins
                {1, 1},
                {1, 2},
                {1, 3},
                {1, 4},
                // Two nodes before the cycle begins
                {2, 1},
                {2, 2},
                {2, 3},
                {2, 4},
                // Three nodes before the cycle begins
                {3, 1},
                {3, 2},
                {3, 3},
                {3, 4},
        };
    }

    @Test(dataProvider = "staticInputProvider")
    public void testStaticInput(int expectedNodesBeforeCycle, int expectedCycleLength) {
        final ListNode head = constructList(expectedNodesBeforeCycle, expectedCycleLength);
        final CycleDetector tortoiseAndHareCycleDetector = new CycleDetector();

        assertCycleDetectorCorrect(head, expectedNodesBeforeCycle, expectedCycleLength, tortoiseAndHareCycleDetector);
    }

    @Test(invocationCount = 10)
    public void testRandomInput() {
        final int expectedNodesBeforeCycle = RANDOM.nextInt(10);
        final int expectedCycleLength = RANDOM.nextInt(10);

        final ListNode head = constructList(expectedNodesBeforeCycle, expectedCycleLength);
        final CycleDetector tortoiseAndHareCycleDetector = new CycleDetector();

        assertCycleDetectorCorrect(head, expectedNodesBeforeCycle, expectedCycleLength, tortoiseAndHareCycleDetector);
    }

    /**
     * Asserts that the {@link CycleDetector} found the correct cycle values.
     *
     * Note that, with zero-indexing, the index of the first node on the cycle is equal to the number of nodes before
     * the cycle.  This fact is used below.
     */
    private void assertCycleDetectorCorrect(ListNode head, int expectedNodesBeforeCycle, int expectedCycleLength, CycleDetector cycleDetector) {
        final Cycle cycle = cycleDetector.detect(head);
        if (cycle == null) {
            Assertions.assertThat(false)
                    .describedAs("Found no cycle when there were %d nodes before a cycle of %d nodes",
                            expectedNodesBeforeCycle, expectedCycleLength)
                    .isEqualTo(expectedCycleLength > 0);
        } else {
            Assertions.assertThat(true)
                    .describedAs("Found a cycle when there were %d nodes terminated by null (0 expectedCycleLength)",
                            expectedNodesBeforeCycle, expectedCycleLength)
                    .isEqualTo(expectedCycleLength > 0);
            int cycleLength = cycle.getCycleLength();
            Assertions.assertThat(cycleLength)
                    .describedAs("Computed cycle length as %d when there were %d nodes before a cycle of %d nodes",
                            cycleLength, expectedNodesBeforeCycle, expectedCycleLength)
                    .isEqualTo(expectedCycleLength);
            int cycleStart = cycle.getIndexOfFirstNodeOnCycle();
            Assertions.assertThat(cycleStart)
                    .describedAs("Computed cycle start as %d when there were %d nodes before a cycle of %d nodes",
                            cycleStart, expectedNodesBeforeCycle, expectedCycleLength)
                    .isEqualTo(expectedNodesBeforeCycle);
        }
    }

    /**
     * Constructs a linked list of nodesBeforeCycle nodes, then appends cycleLength nodes with the final cycle node
     * pointing back to the first cycle node.
     */
    private static ListNode constructList(int nodesBeforeCycle, int cycleLength) {
        assert nodesBeforeCycle >= 0;
        assert cycleLength >= 0;
        final ListNode head = new ListNode(0);
        ListNode tail = head;
        final ListNode startOfCycle;
        final StringBuilder stringBuilder = new StringBuilder(head.toString());

        if (nodesBeforeCycle > 0 && cycleLength > 0) {
            for (int i = 1; i < nodesBeforeCycle; i++) {
                final ListNode newNode = new ListNode(i);
                stringBuilder.append(" -> ").append(newNode);
                tail.setNext(newNode);
                tail = newNode;
            }
            startOfCycle = new ListNode(nodesBeforeCycle);
            stringBuilder.append(" -> ").append(startOfCycle);
            tail.setNext(startOfCycle);
            tail = startOfCycle;
            for (int i = nodesBeforeCycle + 1; i < nodesBeforeCycle + cycleLength; i++) {
                final ListNode newNode = new ListNode(i);
                stringBuilder.append(" -> ").append(newNode);
                tail.setNext(newNode);
                tail = newNode;
            }
            tail.setNext(startOfCycle);
            stringBuilder.append(" -> ").append(startOfCycle);
        } else if (nodesBeforeCycle > 0) {
            for (int i = 1; i < nodesBeforeCycle; i++) {
                final ListNode newNode = new ListNode(i);
                stringBuilder.append(" -> ").append(newNode);
                tail.setNext(newNode);
                tail = newNode;
            }
        } else if (cycleLength > 0) {
            startOfCycle = head;
            for (int i = nodesBeforeCycle + 1; i < nodesBeforeCycle + cycleLength; i++) {
                final ListNode newNode = new ListNode(i);
                stringBuilder.append(" -> ").append(newNode);
                tail.setNext(newNode);
                tail = newNode;
            }
            tail.setNext(startOfCycle);
            stringBuilder.append(" -> ").append(startOfCycle);
        } else {
            logger.debug("Constructed linked list from {} nodesBeforeCycle and {} cycleLength as null",
                    nodesBeforeCycle, cycleLength);
            return null;
        }
        logger.debug("Constructed linked list from {} nodesBeforeCycle and {} cycleLength as {}",
                nodesBeforeCycle, cycleLength, stringBuilder.toString());
        return head;
    }
}
