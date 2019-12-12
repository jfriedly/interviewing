package com.jfriedly.interviewing.practice.tadm2e.polynomial;

/**
 * Evaluates a polynomial of the form ax^n + bx^(n-1) + ... + cx^2 + dx + e
 */
public interface PolynomialEvaluator {

    /**
     * Coefficients are passed from left-to-right.  For ax^n + bx^(n-1) + cx^2 + dx + e, coefficients should be the
     * array [a, b, c, d, e].
     */
    int eval(int x, int[] coefficients);
}
