package com.jfriedly.interviewing.practice.common;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Random;

public class BinaryTreeTest {

    private static final Random RANDOM = new Random();

    @Test
    public void testFullTreeEquality() {
        Assertions.assertThat(fullTree(3))
                .isEqualTo(fullTree(3));
    }

    @Test(invocationCount = 10)
    public void testSeededRandomTreeEquality() {
        final long seed = RANDOM.nextLong();
        RANDOM.setSeed(seed);
        final TreeNode pseudoRandomTree = randomTree(7);
        RANDOM.setSeed(seed);
        Assertions.assertThat(randomTree(7))
                .isEqualTo(pseudoRandomTree);
    }

    @Test(invocationCount = 10)
    public void testRandomTreeInequality() {
        final TreeNode pseudoRandomTree = randomTree(7);
        Assertions.assertThat(randomTree(7))
                .isNotEqualTo(pseudoRandomTree);
    }

    private TreeNode fullTree(int depth) {
        if (depth == 0) {
            return null;
        }
        final TreeNode root = new TreeNode(depth);
        root.setLeft(fullTree(depth - 1));
        root.setLeft(fullTree(depth - 1));
        return root;
    }

    private TreeNode randomTree(int depth) {
        if (depth == 0) {
            return null;
        }
        final TreeNode root = new TreeNode(RANDOM.nextInt());
        if (RANDOM.nextBoolean()) {
            root.setLeft(randomTree(depth - 1));
        }
        if (RANDOM.nextBoolean()) {
            root.setRight(randomTree(depth - 1));
        }
        return root;
    }
}
