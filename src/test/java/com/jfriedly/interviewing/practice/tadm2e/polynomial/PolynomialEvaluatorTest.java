package com.jfriedly.interviewing.practice.tadm2e.polynomial;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Random;

public class PolynomialEvaluatorTest {

    private static final Random RANDOM = new Random();

    @Test
    public void testReferenceImpl() {
        final PolynomialEvaluator referenceImpl = new SimplePolynomialEvaluator();
        final int inputX = 42;
        final int[] inputCoefficients = new int[]{1, 2, 3, 4, 5};

        Assertions.assertThat(referenceImpl.eval(inputX, inputCoefficients))
                .isEqualTo(3265337);
    }

    @Test(invocationCount = 100)
    public void testRandomInputs() {
        final PolynomialEvaluator testImpl = new HornersPolynomialEvaluator();
        final PolynomialEvaluator referenceImpl = new SimplePolynomialEvaluator();

        final int inputX = RANDOM.nextBoolean() ? RANDOM.nextInt(10) : -RANDOM.nextInt(10);
        final int[] inputCoefficients = RANDOM.ints(5, -100, 100)
                .toArray();

        Assertions.assertThat(testImpl.eval(inputX, inputCoefficients))
                .describedAs("x = %d, coefficients = %s", inputX, inputCoefficients)
                .isEqualTo(referenceImpl.eval(inputX, inputCoefficients));
    }
}
