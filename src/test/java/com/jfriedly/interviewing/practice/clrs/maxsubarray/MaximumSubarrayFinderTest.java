package com.jfriedly.interviewing.practice.clrs.maxsubarray;

import org.assertj.core.api.Assertions;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Random;

public class MaximumSubarrayFinderTest {

    private static final Random RANDOM = new Random();

    @DataProvider
    public Object[][] staticInputProvider() {
        // Format is (impl, array, expectedSum, expectedStart, expectedEnd)
        return new Object[][] {
                { new NChoose2SubarrayFinder(), new int[] {42}, 0, 1, 42},
                // Example input from CLRS, section 4.1, page 69
                { new NChoose2SubarrayFinder(), new int[] {0,1,-4,3,-4}, 3, 4, 3},
                // Example input from CLRS, section 4.1, page 68
                { new NChoose2SubarrayFinder(), new int[] {0,13,-3,-25,20,-3,-16,-23,18,20,-7,12,-5,-22,15,-4,7}, 8, 12, 43},
                // Example from leetcode
                { new NChoose2SubarrayFinder(), new int[] {-1, -1, -2, -2}, 0, 1, -1},

                { new LinearSubarrayFinder(), new int[] {42}, 0, 1, 42},
                { new LinearSubarrayFinder(), new int[] {0,1,-4,3,-4}, 3, 4, 3},
                { new LinearSubarrayFinder(), new int[] {0,13,-3,-25,20,-3,-16,-23,18,20,-7,12,-5,-22,15,-4,7}, 8, 12, 43},
                { new LinearSubarrayFinder(), new int[] {-1, -1, -2, -2}, 0, 1, -1},

                { new DivideAndConquerSubarrayFinder(), new int[] {42}, 0, 1, 42},
                { new DivideAndConquerSubarrayFinder(), new int[] {0,1,-4,3,-4}, 3, 4, 3},
                { new DivideAndConquerSubarrayFinder(), new int[] {0,13,-3,-25,20,-3,-16,-23,18,20,-7,12,-5,-22,15,-4,7}, 8, 12, 43},
                { new DivideAndConquerSubarrayFinder(), new int[] {-1, -1, -2, -2}, 0, 1, -1},
        };
    }

    @DataProvider
    public Object[][] implProvider() {
        // Format is (referenceImpl, testImpl)
        return new Object[][] {
                { new NChoose2SubarrayFinder(), new LinearSubarrayFinder() },
                { new NChoose2SubarrayFinder(), new DivideAndConquerSubarrayFinder() },
        };
    }

    @Test(dataProvider = "staticInputProvider")
    public void testStaticInputs(MaximumSubarrayFinder finder,
                                 int[] inputArray,
                                 int expectedStart,
                                 int expectedEnd,
                                 int expectedSum) {
        final Subarray actual = finder.find(inputArray);
        final Subarray expected = new Subarray(inputArray, expectedStart, expectedEnd, expectedSum);
        Assertions.assertThat(actual)
                .isEqualTo(expected);
    }

    @Test(invocationCount = 10, dataProvider = "implProvider")
    public void testRandomInputs(MaximumSubarrayFinder referenceFinder, MaximumSubarrayFinder testFinder) {
        final int[] input = RANDOM.ints(100, -50, 50).toArray();

        final Subarray actual = testFinder.find(input);
        final Subarray expected = referenceFinder.find(input);

        Assertions.assertThat(actual)
                .isEqualTo(expected);
    }

}
