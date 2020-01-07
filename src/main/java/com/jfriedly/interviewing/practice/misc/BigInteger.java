package com.jfriedly.interviewing.practice.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BigInteger {
    private static final Logger logger = LoggerFactory.getLogger(BigInteger.class);
    private static final int SIZE = 100;

    private final byte[] arr = new byte[SIZE];

    public static BigInteger fromString(String bigString) {
        BigInteger b = new BigInteger();
        int j = SIZE - 1;
        for (int i = bigString.length() - 1; i >= 0; i--) {
            final char c = bigString.charAt(i);
            switch (c) {
                case '0':
                    b.arr[j] = 0;
                    break;
                case '1':
                    b.arr[j] = 1;
                    break;
                case '2':
                    b.arr[j] = 2;
                    break;
                case '3':
                    b.arr[j] = 3;
                    break;
                case '4':
                    b.arr[j] = 4;
                    break;
                case '5':
                    b.arr[j] = 5;
                    break;
                case '6':
                    b.arr[j] = 6;
                    break;
                case '7':
                    b.arr[j] = 7;
                    break;
                case '8':
                    b.arr[j] = 8;
                    break;
                case '9':
                    b.arr[j] = 9;
                    break;
                default:
                    throw new IllegalArgumentException("Cannot construct BigInteger from char '" + c + "'");
            }
            j--;
        }
        return b;
    }

    public BigInteger add(BigInteger other) {
        final BigInteger sum = new BigInteger();
        int i = SIZE - 1;
        boolean carry = false;
        while (i >= 0) {
            int subSum = arr[i] + other.arr[i];
            if (carry) {
                subSum++;
            }
            assert 0 <= subSum && subSum < 20;
            if (subSum >=  10) {
                carry = true;
                subSum %= 10;
            } else {
                carry = false;
            }
            sum.arr[i] = (byte) subSum;
            i--;
        }
        if (carry) {
            throw new RuntimeException("integer overflow");
        }
        return sum;
    }

    public BigInteger subtract(BigInteger other) {
        final BigInteger difference = new BigInteger();
        int i = SIZE - 1;
        boolean carry = false;
        while (i >= 0) {
            int subDifference = arr[i] - other.arr[i];
            if (carry) {
                subDifference--;
            }
            assert -10 <= subDifference && subDifference < 10;
            if (subDifference < 0) {
                carry = true;
                subDifference = Math.floorMod(subDifference, 10);
            } else {
                carry = false;
            }
            difference.arr[i] = (byte) subDifference;
            i--;
        }
        if (carry) {
            throw new RuntimeException("integer overflow");
        }
        return difference;
    }

    public BigInteger multiply(BigInteger other) {
        BigInteger product = new BigInteger();
        for (int i = SIZE - 1; i >= 0; i--) {
            final BigInteger subProduct = new BigInteger();
            int carry = 0;
            for (int j = SIZE - 1; j - (SIZE - 1 - i) >= 0; j--) {
                int subSubProduct = other.arr[i] * arr[j];
                logger.debug("subSubProduct is {}", subSubProduct);
                if (carry > 0) {
                    subSubProduct += carry % 10;
                }
                if (subSubProduct >= 10) {
                    carry = subSubProduct / 10;
                    subSubProduct %= 10;
                } else {
                    carry = 0;
                }
                logger.debug("setting subproduct[{}] to {}", j, subSubProduct);
                subProduct.arr[j - (SIZE - 1 - i)] = (byte) subSubProduct;
            }
            if (carry > 0) {
                throw new RuntimeException("integer overflow");
            }
            logger.debug("Subproduct is {}", subProduct);
            product = product.add(subProduct);
        }
        return product;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i;
        for (i = 0; i < SIZE && arr[i] == 0; i++) {}
        while(i < SIZE) {
            sb.append(arr[i]);
            i++;
        }
        return sb.toString();
    }

}
