package com.jfriedly.interviewing.practice.clrs.maxsubarray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Divide-and-conquer approach to finding the maximum subarray, as described in CLRS section 4.1.  O(n log n).
 *
 * Similar to merge sort, we can break down the maximum subarray problem in halves, recursively checking the left
 * and right halves until we reach a base case array size of 1.  But, likewise similar to merge sort, there's then
 * a step following where we have to "merge" the solutions from the left and right subarrays.  It's not sufficient
 * to merely take the larger, because that would just find the largest element; we have to also check if the largest
 * subarray crosses the boundary between the left and right halves.  Fortunately, that can be done in O(n), and with
 * the tree depth at O(log n), the entire problem can be solved in O(n log n).
 */
public class DivideAndConquerSubarrayFinder implements MaximumSubarrayFinder {

    private static final Logger logger = LoggerFactory.getLogger(DivideAndConquerSubarrayFinder.class);

    @Override
    public Subarray find(int[] array) {
        return findRecursive(new Subarray(array, 0, array.length));
    }

    private Subarray findRecursive(Subarray subarray) {
        if (subarray.getEnd() - subarray.getStart() <= 1) {
            return subarray;
        }
        final int mid = (subarray.getEnd() + subarray.getStart()) / 2;
        final Subarray left = new Subarray(subarray.getEnclosingArray(), subarray.getStart(), mid);
        final Subarray right = new Subarray(subarray.getEnclosingArray(), mid, subarray.getEnd());
        final Subarray largestLeft = findRecursive(left);
        final Subarray largestRight = findRecursive(right);
        final Subarray largestCrossing = findLargestSubarrayCrossingBoundary(subarray.getEnclosingArray(), left, right);
        if (largestLeft.getSum() >= largestCrossing.getSum() && largestLeft.getSum() >= largestRight.getSum()) {
            return largestLeft;
        } else if (largestRight.getSum() >= largestCrossing.getSum() && largestRight.getSum() >= largestLeft.getSum()) {
            return largestRight;
        } else {
            return largestCrossing;
        }
    }

    private Subarray findLargestSubarrayCrossingBoundary(int[] enclosingArray, Subarray left, Subarray right) {
        // The largest subarray crossing the boundary between left and right subarray halves must include both the
        // last element in the left subarray and first element in the right subarray.
        Subarray largestCrossingSoFar = new Subarray(enclosingArray, left.getEnd() - 1, right.getStart() + 1);
        // Working from the end of the left array backwards, check each element to see if we can expand
        // largestCrossingSoFar.
        int leftSum = largestCrossingSoFar.getSum();
        for (int i = left.getEnd() - 2; i >= left.getStart(); i--) {
            leftSum += enclosingArray[i];
            if (largestCrossingSoFar.getSum() <= leftSum) {
                largestCrossingSoFar = new Subarray(enclosingArray, i, largestCrossingSoFar.getEnd(), leftSum);
            }
        }
        // Working from the start of the right array forwards, check each element to see if we can expand
        // largestCrossingSoFar.
        int rightSum = largestCrossingSoFar.getSum();
        for (int i = right.getStart() + 1; i < right.getEnd(); i++) {
            rightSum += enclosingArray[i];
            if (largestCrossingSoFar.getSum() <= rightSum) {
                largestCrossingSoFar = new Subarray(enclosingArray, largestCrossingSoFar.getStart(), i + 1, rightSum);
            }
        }
        //logger.debug("Returning largestCrossingSoFar from [{}:{}] to [{}:{}] as {}", left.getStart(), left.getEnd(),
        //        right.getStart(), right.getEnd(), largestCrossingSoFar);
        return largestCrossingSoFar;
    }
}
