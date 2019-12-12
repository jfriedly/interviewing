package com.jfriedly.interviewing.practice.generic.sorting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class MergeSort implements Sorter {

    private static final Logger logger = LoggerFactory.getLogger(MergeSort.class);

    @Override
    public void sort(int[] array) {
        mergeSortCountingInversions(array);
    }

    /* pp */ int mergeSortCountingInversions(int[] array) {
        return mergeSortCountingInversions(array, 0, array.length);
    }

    /**
     * Merge sort the sub array specified by start and end but also count the number of inversions, as defined by
     * CLRS Problem 2-4.
     *
     * Inversions are calculated by incrementing a counter by the number of remaining elements in the left subarray
     * every time an element from the right subarray is selected.
     *
     * @param array the "real" array containing the array to be sorted
     * @param start inclusive lower index of the subarray
     * @param end   exclusive upper index of the subarrray
     */
    private int mergeSortCountingInversions(int[] array, int start, int end) {
        //logger.debug("Merge sorting {}", Arrays.copyOfRange(array, start, end));
        int inversions = 0;
        if (end - start <= 1) {
            return inversions;
        }
        final int mid = (start + end) / 2;
        inversions += mergeSortCountingInversions(array, start, mid);
        inversions += mergeSortCountingInversions(array, mid, end);
        inversions += mergeCountingInversions(array, start, mid, end);
        return inversions;
    }

    /**
     * Merge the two sorted sub arrays specified by lstart, mid, and rend
     * @param array  the "real" array containing the two subarrays
     * @param lstart inclusive lower index of the left subarray
     * @param mid    the boundary between the left and right subarrays.  Inclusive for the right subarray.
     * @param rend   exclusive upper index of the right subarray
     */
    private int mergeCountingInversions(int[] array, int lstart, int mid, int rend) {
        int inversions = 0;
        // Note:  it's not actually necessary to copy the right subarray.
        // The loop below can't overwrite elements in the right subarray until after they've been used:  either the
        // left subarray contains all of the smallest elements, or there's a one-for-one relationship between small
        // elements drawn from the right subarray and the new effective lower bound of the sub array.
        // This also means that it's not necessary to check j == left.length and potentially copy elements from the
        // right subarray back into the array, because you'd just be copying identical indexes from array to array.
        // When the loop exits with j == left.length, i == k.
        final int[] left = Arrays.copyOfRange(array, lstart, mid);
        //logger.debug("Merging left: {} with right: {}", left, Arrays.copyOfRange(array, mid, rend));
        int i = lstart;
        int j = 0;
        int k = mid;
        while (j < left.length && k < rend) {
            // Note: take from the left array on <= not just <.
            // If you take from the right array below on ==, you'll sort correctly, but your inversion count will be
            // wrong.
            if (left[j] <= array[k]) {
                array[i] = left[j];
                j++;
            } else {
                array[i] = array[k];
                k++;
                // Increment inversions by the number of elements that were right of this element
                inversions += left.length - j;
            }
            i++;
        }
        // Copy any remaining elements from the duplicate left subarray into the source array, in order.
        if (k == rend) {
            while (j < left.length) {
                array[i] = left[j];
                i++;
                j++;
            }
        }
        return inversions;
    }
}
