package com.jfriedly.interviewing.practice.tadm2e;

import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Random;

public class LoopFinderTest {

    private static final Logger logger = LoggerFactory.getLogger(LoopFinderTest.class);
    private static final Random RANDOM = new Random();

    /**
     * Returns pairs of integers that can be interpreted as the number of nodes before a loop and the number of nodes
     * on the loop.  Literature refers to the first value as *mu* and the second as *lambda*.  Zero nodes before the
     * loop indicates that the entire linked list should be a loop (i.e. a circular list), zero nodes on the loop
     * indicates that there should not actually be a loop, and both values as zero indicates that the list is simply
     * null.  Negative values are not allowed.
     *
     * Note that, with zero-indexing, the index of the first node on the loop is equal to the number of nodes before
     * the loop.
     */
    @DataProvider
    public Object[][] staticInputProvider() {
        return new Object[][] {
                // Null linked list
                {0, 0},
                // No loop linked lists
                {1, 0},
                {2, 0},
                {3, 0},
                // No nodes before the loop starts, i.e. the list is circular
                {0, 1},
                {0, 2},
                {0, 3},
                // A single node before the loop begins
                {1, 1},
                {1, 2},
                {1, 3},
                {1, 4},
                // Two nodes before the loop begins
                {2, 1},
                {2, 2},
                {2, 3},
                {2, 4},
                // Three nodes before the loop begins
                {3, 1},
                {3, 2},
                {3, 3},
                {3, 4},
        };
    }

    @Test(dataProvider = "staticInputProvider")
    public void testStaticInput(int expectedNodesBeforeLoop, int expectedLoopLength) {
        final LoopFinder.ListNode head = constructList(expectedNodesBeforeLoop, expectedLoopLength);
        final LoopFinder loopFinder = new LoopFinder(head);

        assertLoopFinderCorrect(expectedNodesBeforeLoop, expectedLoopLength, loopFinder);
    }

    @Test(invocationCount = 10)
    public void testRandomInput() {
        final int expectedNodesBeforeLoop = RANDOM.nextInt(10);
        final int expectedLoopLength = RANDOM.nextInt(10);

        final LoopFinder.ListNode head = constructList(expectedNodesBeforeLoop, expectedLoopLength);
        final LoopFinder loopFinder = new LoopFinder(head);

        assertLoopFinderCorrect(expectedNodesBeforeLoop, expectedLoopLength, loopFinder);
    }

    /**
     * Asserts that the {@link LoopFinder} found the correct loop values.
     *
     * Note that, with zero-indexing, the index of the first node on the loop is equal to the number of nodes before
     * the loop.  This fact is used below.
     */
    private void assertLoopFinderCorrect(int expectedNodesBeforeLoop, int expectedLoopLength, LoopFinder loopFinder) {
        if (loopFinder.loopExists()) {
            Assertions.assertThat(true)
                    .describedAs("Found a loop when there were %d nodes terminated by null (0 expectedLoopLength)",
                            expectedNodesBeforeLoop, expectedLoopLength)
                    .isEqualTo(expectedLoopLength > 0);
            int loopLength = loopFinder.loopLength();
            Assertions.assertThat(loopLength)
                    .describedAs("Computed loop length as %d when there were %d nodes before a loop of %d nodes",
                            loopLength, expectedNodesBeforeLoop, expectedLoopLength)
                    .isEqualTo(expectedLoopLength);
            int loopStart = loopFinder.loopStart();
            Assertions.assertThat(loopStart)
                    .describedAs("Computed loop start as %d when there were %d nodes before a loop of %d nodes",
                            loopStart, expectedNodesBeforeLoop, expectedLoopLength)
                    .isEqualTo(expectedNodesBeforeLoop);
        } else {
            Assertions.assertThat(false)
                    .describedAs("Found no loop when there were %d nodes before a loop of %d nodes",
                            expectedNodesBeforeLoop, expectedLoopLength)
                    .isEqualTo(expectedLoopLength > 0);
        }
    }

    /**
     * Constructs a linked list of nodesBeforeLoop nodes, then appends loopLength nodes with the final loop node
     * pointing back to the first loop node.
     */
    private static LoopFinder.ListNode constructList(int nodesBeforeLoop, int loopLength) {
        assert nodesBeforeLoop >= 0;
        assert loopLength >= 0;
        final LoopFinder.ListNode head = new LoopFinder.ListNode(0);
        LoopFinder.ListNode tail = head;
        final LoopFinder.ListNode startOfLoop;
        final StringBuilder stringBuilder = new StringBuilder(head.toString());

        if (nodesBeforeLoop > 0 && loopLength > 0) {
            for (int i = 1; i < nodesBeforeLoop; i++) {
                final LoopFinder.ListNode newNode = new LoopFinder.ListNode(i);
                stringBuilder.append(" -> ").append(newNode);
                tail.setNext(newNode);
                tail = newNode;
            }
            startOfLoop = new LoopFinder.ListNode(nodesBeforeLoop);
            stringBuilder.append(" -> ").append(startOfLoop);
            tail.setNext(startOfLoop);
            tail = startOfLoop;
            for (int i = nodesBeforeLoop + 1; i < nodesBeforeLoop + loopLength; i++) {
                final LoopFinder.ListNode newNode = new LoopFinder.ListNode(i);
                stringBuilder.append(" -> ").append(newNode);
                tail.setNext(newNode);
                tail = newNode;
            }
            tail.setNext(startOfLoop);
            stringBuilder.append(" -> ").append(startOfLoop);
        } else if (nodesBeforeLoop > 0) {
            for (int i = 1; i < nodesBeforeLoop; i++) {
                final LoopFinder.ListNode newNode = new LoopFinder.ListNode(i);
                stringBuilder.append(" -> ").append(newNode);
                tail.setNext(newNode);
                tail = newNode;
            }
        } else if (loopLength > 0) {
            startOfLoop = head;
            for (int i = nodesBeforeLoop + 1; i < nodesBeforeLoop + loopLength; i++) {
                final LoopFinder.ListNode newNode = new LoopFinder.ListNode(i);
                stringBuilder.append(" -> ").append(newNode);
                tail.setNext(newNode);
                tail = newNode;
            }
            tail.setNext(startOfLoop);
            stringBuilder.append(" -> ").append(startOfLoop);
        } else {
            logger.debug("Constructed linked list from {} nodesBeforeLoop and {} loopLength as null",
                    nodesBeforeLoop, loopLength);
            return null;
        }
        logger.debug("Constructed linked list from {} nodesBeforeLoop and {} loopLength as {}",
                nodesBeforeLoop, loopLength, stringBuilder.toString());
        return head;
    }
}
