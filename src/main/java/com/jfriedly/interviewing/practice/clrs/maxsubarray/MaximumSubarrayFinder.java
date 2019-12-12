package com.jfriedly.interviewing.practice.clrs.maxsubarray;

public interface MaximumSubarrayFinder {

    /**
     * Returns a largest subarray in the given array.
     *
     * A subarray is a contiguous series of indexes in the given array.  A largest subarray is one that has
     * the largest sum.  For example, if the array is [0, 1, -4, 3, -4], then the largest subarray is the including
     * only index 3:  [3] with sum 3.
     *
     * Multiple "largest" subarrays may exist in the array; i.e. there may be more than one way to reach the same
     * largest sum.  If the array contains multiple largest subarrays, implementations of this method may return
     * any of them.
     *
     * Note:  for any array that only contains non-negative values, a largest subarray will simply be the entire
     * array.  This method is only useful if the array contains negative values.
     */
    Subarray find(int[] array);
}
