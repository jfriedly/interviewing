package com.jfriedly.interviewing.practice.tadm2e.polynomial;

/**
 * Polynomial evaluator that implements Horner's method:  https://en.wikipedia.org/wiki/Horner%27s_method
 */
public class HornersPolynomialEvaluator implements PolynomialEvaluator {

    @Override
    public int eval(int x, int[] coefficients) {
        int result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result = x * result + coefficients[i];
        }
        return result;
    }
}
