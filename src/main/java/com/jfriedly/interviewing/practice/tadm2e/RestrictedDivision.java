package com.jfriedly.interviewing.practice.tadm2e;

/**
 * Performs integer division without using either the {@code *} or {@code /} operators.  Exercise from Scheiner,
 * problem 1-27.
 */
public class RestrictedDivision {

    /**
     * Integer division, where the remainder is always truncated.
     */
    public static int divide(int numerator, int denominator) {
        if (numerator < 0 && denominator < 0) {
            return dividePositive(-numerator, -denominator);
        } else if (numerator < 0) {
            return -dividePositive(-numerator, denominator);
        } else if (denominator < 0) {
            return -dividePositive(numerator, -denominator);
        } else {
            return dividePositive(numerator, denominator);
        }
    }

    /**
     * Recursive implementation of positive integer division.
     *
     * Note:  uses a recursive tail call, so could be translated into a loop.
     */
    private static int dividePositive(int numerator, int denominator) {
        if (denominator == 0) {
            throw new ArithmeticException("Division by zero");
        }
        if (numerator < denominator) {
            return 0;
        } else if (numerator == denominator) {
            return 1;
        }
        int answer = 1;
        int denominatorCopy = denominator;
        while (numerator > denominatorCopy && denominatorCopy < (Integer.MAX_VALUE >> 2)) {
            denominatorCopy <<= 1;
            answer <<= 1;
        }
        // The loop generally exits when we've gone one step too far (denominatorCopy > numerator), so undo that
        if (denominatorCopy > numerator) {
            denominatorCopy >>= 1;
            answer >>= 1;
        }
        // Now we have the largest power of two times the denominator that fits into the numerator.  Subtract that from
        // the numerator, repeat with the remaining numerator, and sum all of the answers together.
        final int difference = numerator - denominatorCopy;
        return answer + dividePositive(difference, denominator);
    }
}
