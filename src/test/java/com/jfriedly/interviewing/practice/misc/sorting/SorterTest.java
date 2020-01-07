package com.jfriedly.interviewing.practice.misc.sorting;

import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Random;

public class SorterTest {

    private static final Logger logger = LoggerFactory.getLogger(SorterTest.class);
    private static final Random RANDOM = new Random();

    @DataProvider
    private Object[][] sorterImplsProvider() {
        return new Object[][] {
                { new QuickSort() },
                { new InsertionSort() },
                { new BubbleSort() },
                { new MergeSort() },
                { new HeapSort() },
        };
    }

    @Test(invocationCount = 10, dataProvider = "sorterImplsProvider")
    public void testSort(Sorter sorter) {
        final int[] input = RANDOM.ints(RANDOM.nextInt(100), 0, 100).toArray();
        final int[] actual = Arrays.copyOf(input, input.length);
        sorter.sort(actual);
        int[] expected = Arrays.copyOf(input, input.length);
        Arrays.sort(expected);

        Assertions.assertThat(actual)
                .isEqualTo(expected);
    }

    @Test(invocationCount = 10, dataProvider = "sorterImplsProvider")
    public void testSmallestK(Sorter sorter) {
        final int[] input = RANDOM.ints(RANDOM.nextInt(100), 0, 100).toArray();
        final int k = RANDOM.nextInt(42);
        final int[] actual = sorter.smallestK(Arrays.copyOf(input, input.length), k);
        int[] sorted = Arrays.copyOf(input, input.length);
        Arrays.sort(sorted);
        final int[] expected = Arrays.copyOf(sorted, k);

        Assertions.assertThat(actual)
                .containsExactlyInAnyOrder(expected);
    }

    /**
     * Test implementations of inversion counting, as defined by CLRS Problem 2-4.
     *
     * Outcome of this test:
     * The merge sort approach of counting just the *remaining* elements in the left subarray every time you take an
     * element from the right subarray does indeed work.  The authors of CLRS give an approach similar to this in
     * their published textbook answers, but it's not quite the same:
     * http://mitp-content-server.mit.edu:18180/books/content/sectbyfn?collid=books_pres_0&id=8030&fn=Intro_to_Algo_Selected_Solutions.pdf
     * Their approach, notably, uses a boolean to tell if the current variable has already been counted, but I'm
     * pretty sure that their boolean is always immediately flipped back to false.  I'm also pretty sure that the
     * implementation in {@link MergeSort} correctly demonstrates that there's no need for a boolean flag like this
     * although it is notable that I had to change the top condition in the while loop from {@code <} to {@code <=} to
     * make it work.
     */
    @Test(invocationCount = 10)
    public void testInversionCounting() {
        final InsertionSort insertionSort = new InsertionSort();
        final MergeSort mergeSort = new MergeSort();
        final int[] input = RANDOM.ints(RANDOM.nextInt(100), 0, 100).toArray();
        //final int[] input = new int[]{2,1,1,1};
        final int expected = insertionSort.sortCountingInversions(Arrays.copyOf(input, input.length));
        final int actual = mergeSort.mergeSortCountingInversions(Arrays.copyOf(input, input.length));

        Assertions.assertThat(actual)
                .isEqualTo(expected);
    }
}
