package com.jfriedly.interviewing.practice.tadm2e;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Random;

public class RestrictedDivisionTest {

    private static final Random RANDOM = new Random();

    @Test(invocationCount = 100)
    public void testRandomInputs() {
        final int numerator = RANDOM.nextBoolean() ? RANDOM.nextInt(100) : -RANDOM.nextInt(100);
        final int denominator = RANDOM.nextBoolean() ? RANDOM.nextInt(10) + 1 : -(RANDOM.nextInt(10) + 1);

        final int actual = RestrictedDivision.divide(numerator, denominator);
        final int expected = numerator / denominator;

        Assertions.assertThat(actual)
                .describedAs("%d / %d", numerator, denominator)
                .isEqualTo(expected);
    }

    /**
     * This test takes a little while if you use a O(n) approach (about 1 second on my laptop).  It executes in a few
     * milliseconds using a O(log n) approach.
     */
    @Test
    public void testLargeInput() {
        final int numerator = Integer.MAX_VALUE;
        final int denominator = 1;

        final int actual = RestrictedDivision.divide(numerator, denominator);

        Assertions.assertThat(actual)
                .describedAs("%d / %d", numerator, denominator)
                .isEqualTo(Integer.MAX_VALUE);
    }
}
