package com.jfriedly.interviewing.practice.tadm2e.polynomial;

public class SimplePolynomialEvaluator implements PolynomialEvaluator {

    @Override
    public int eval(int x, int[] coefficients) {
        int result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, coefficients.length - i - 1);
        }
        return result;
    }
}
