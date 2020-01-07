package com.jfriedly.interviewing.practice.misc;

import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

public class BigIntegerTest {
    private static final Logger logger = LoggerFactory.getLogger(BigIntegerTest.class);
    private static final String BIG_STRING_1 = "74758238738596937289";
    private static final String BIG_STRING_2 = "394758739837495073";

    @Test
    public void testFromString() {
        final String first = "42";
        logger.info("Parsed {} as {}", first, BigInteger.fromString(first));
        final String second = "7628485948372839496";
        logger.info("Parsed {} as {}", second, BigInteger.fromString(second));
    }

    @Test
    public void testAddition() {
        final BigInteger first = BigInteger.fromString("42");
        final BigInteger second = BigInteger.fromString("24");
        logger.info("Added 42 + 24: {}", first.add(second));

        final BigInteger third = BigInteger.fromString(BIG_STRING_1);
        final java.math.BigInteger langThird = new java.math.BigInteger(BIG_STRING_1);
        final BigInteger fourth = BigInteger.fromString(BIG_STRING_2);
        final java.math.BigInteger langFourth = new java.math.BigInteger(BIG_STRING_2);
        logger.info("Added {} and {} as {}", BIG_STRING_1, BIG_STRING_2, third.add(fourth));
        logger.info("Lang adds these as {}", langThird.add(langFourth));
        Assertions.assertThat(third.add(fourth).toString())
                .isEqualTo(langThird.add(langFourth).toString());
    }

    @Test
    public void testSubtraction() {
        final BigInteger first = BigInteger.fromString("42");
        final BigInteger second = BigInteger.fromString("24");
        logger.info("Subtracted 42 - 24: {}", first.subtract(second));

        final BigInteger third = BigInteger.fromString(BIG_STRING_1);
        final java.math.BigInteger langThird = new java.math.BigInteger(BIG_STRING_1);
        final BigInteger fourth = BigInteger.fromString(BIG_STRING_2);
        final java.math.BigInteger langFourth = new java.math.BigInteger(BIG_STRING_2);
        logger.info("Subtracted {} and {} as {}", BIG_STRING_1, BIG_STRING_2, third.subtract(fourth));
        logger.info("Lang subtracts these as {}", langThird.subtract(langFourth));
        Assertions.assertThat(third.subtract(fourth).toString())
                .isEqualTo(langThird.subtract(langFourth).toString());
    }

    @Test
    public void testMultiplication() {
        final BigInteger first = BigInteger.fromString("42");
        final BigInteger second = BigInteger.fromString("24");
        logger.info("Multiplied 42 * 24: {}", first.multiply(second));

        final BigInteger third = BigInteger.fromString(BIG_STRING_1);
        final java.math.BigInteger langThird = new java.math.BigInteger(BIG_STRING_1);
        final BigInteger fourth = BigInteger.fromString(BIG_STRING_2);
        final java.math.BigInteger langFourth = new java.math.BigInteger(BIG_STRING_2);
        logger.info("Multiplied {} and {} as {}", BIG_STRING_1, BIG_STRING_2, third.multiply(fourth));
        logger.info("Lang multiplies these as {}", langThird.multiply(langFourth));
        Assertions.assertThat(third.multiply(fourth).toString())
                .isEqualTo(langThird.multiply(langFourth).toString());
    }
}
