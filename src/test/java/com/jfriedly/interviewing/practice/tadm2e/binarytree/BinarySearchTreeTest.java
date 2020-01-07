package com.jfriedly.interviewing.practice.tadm2e.binarytree;

import com.jfriedly.interviewing.practice.common.ListNode;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.*;

/**
 * Equality tests here are written for Skeiner problem 3-21, page 101.
 */
public class BinarySearchTreeTest {

    private static final Logger logger = LoggerFactory.getLogger(BinarySearchTreeTest.class);
    private static final Random RANDOM = new Random();

    @Test(invocationCount = 10)
    public void testRandomBSTSetBehavior() {
        final int nodes = RANDOM.nextInt(10);
        final BinarySearchTree bst = new BinarySearchTree();
        final Set<Integer> expected = new TreeSet<>();

        for (int i = 0; i < nodes; i++) {
            final int value = RANDOM.nextInt();
            bst.insert(value);
            expected.add(value);
        }

        for (Integer value : expected) {
            Assertions.assertThat(bst.search(value))
                    .isTrue();
        }
    }

    @Test(invocationCount = 10)
    public void testRandomBSTToLinkedList() {
        final int nodes = RANDOM.nextInt(10);
        final BinarySearchTree bst = new BinarySearchTree();
        final Set<Integer> expected = new TreeSet<>();

        for (int i = 0; i < nodes; i++) {
            final int value = RANDOM.nextInt();
            logger.debug("Inserting {}", value);
            bst.insert(value);
            expected.add(value);
        }

        logger.debug("BST is {}", bst);
        // Note:  TreeSet will return the elements in sorted order.  So will our toLinkedList() method.
        logger.debug("Expected tree set is {}", expected);
        ListNode actual = bst.toLinkedList();
        for (Integer value : expected) {
            logger.debug("Asserting that {} is equal to {}", actual.getId(), value);
            Assertions.assertThat(actual.getId())
                    .isEqualTo(value);
            actual = actual.getNext();
        }
    }
}
