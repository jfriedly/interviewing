package com.jfriedly.interviewing.practice.clrs.maxsubarray;

/**
 * Reference implementation of {@link MaximumSubarrayFinder}.
 *
 * This implementation is straightforward, but inefficient.  It simply checks every possible subarray and returns
 * the one with the greatest sum, which is equivalent to checking n choose 2 subarrays.  The running time is O(n^2).
 */
public class NChoose2SubarrayFinder implements MaximumSubarrayFinder {

    @Override
    public Subarray find(int[] array) {
        Subarray largestSoFar = new Subarray(array, 0, array.length);
        for (int start = 0; start < array.length; start++) {
            int currentSum = 0;
            for (int end = start + 1; end <= array.length; end++) {
                currentSum += array[end - 1];
                final Subarray currentSubarray = new Subarray(array, start, end, currentSum);
                if (largestSoFar.getSum() < currentSum) {
                    largestSoFar = currentSubarray;
                }
            }
        }
        return largestSoFar;
    }
}
