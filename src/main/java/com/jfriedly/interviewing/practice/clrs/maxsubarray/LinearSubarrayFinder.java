package com.jfriedly.interviewing.practice.clrs.maxsubarray;

/**
 * Linear time maximum subarray finder.
 *
 * This implementation follows the approach suggested in CLRS exercise 4.1-5:  it proceeds form left to right in
 * the array keeping track of the largest subarray seen so far. Every time the loop increments, the new largest
 * subarray will either be the largest one that we had already seen, or it will be one including the new element being
 * examined.  The largest subarray including the new element being examined will always be either the one that we
 * currently have plus the new element (whenever the current one has a nonnegative sum) or it will simply be the new
 * element by itself (whenever the current one has a negative sum).  Knowing the largest subarray that includes the
 * current element, we can trivially update our largest subarray so far if its bigger.
 */
public class LinearSubarrayFinder implements MaximumSubarrayFinder {

    @Override
    public Subarray find(int[] array) {
        Subarray largestSoFar = new Subarray(array, 0, 1);
        Subarray largestEndingAfterI = new Subarray(array, 0, 1, 0);
        for (int i = 0; i < array.length; i++) {
            if (largestEndingAfterI.getSum() < 0) {
                largestEndingAfterI = new Subarray(array, i, i + 1, array[i]);
            } else {
                largestEndingAfterI = new Subarray(array, largestEndingAfterI.getStart(), i + 1, largestEndingAfterI.getSum() + array[i]);
            }

            if (largestSoFar.getSum() < largestEndingAfterI.getSum()) {
                largestSoFar = largestEndingAfterI;
            }
        }
        return largestSoFar;
    }

    // Note:  this impl is pretty fast, but if you just track bare integers instead of using nice Subarray objects,
    // it's even faster.  The conversion is straightforward.  See converted solution at
    // https://leetcode.com/problems/maximum-subarray/submissions/
}
