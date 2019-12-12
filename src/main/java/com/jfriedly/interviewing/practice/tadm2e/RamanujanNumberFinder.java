package com.jfriedly.interviewing.practice.tadm2e;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Finds Ramanujan numbers, ones that can be expressed as the sum of two cubes in two different ways (Skeiner problem
 * 2-50, page 64).
 *
 * All of the Ramanujan numbers up to the argument n are reported.  Note that this implementation ignores cubes of
 * negative numbers, in keeping with the usual definition for Taxicab numbers.  Taxicab numbers, however, require
 * that the number be expressible as a sum of two cubes in *m* different ways and refer specifically to the smallest
 * such number.  Ramanujan numbers, on the other hand, assume that m=2 and refer to all numbers that satisfy the sum
 * constraint.
 */
public class RamanujanNumberFinder {

    private static final Logger logger = LoggerFactory.getLogger(RamanujanNumberFinder.class);

    static class CubeSum {
        private final int a;
        private final int b;

        public CubeSum(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public int sum() {
            return Double.valueOf(Math.pow(a, 3) + Math.pow(b, 3)).intValue();
        }

        @Override
        public String toString() {
            return String.format("%d^3 + %d^3 = %d", a, b, sum());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CubeSum cubeSum = (CubeSum) o;
            return (a == cubeSum.a && b == cubeSum.b) || (a == cubeSum.b && b == cubeSum.a);
        }

        @Override
        public int hashCode() {
            return Objects.hash(a, b);
        }
    }

    static class RamanujanNumber {
        private final CubeSum first;
        private final CubeSum second;

        public RamanujanNumber(CubeSum first, CubeSum second) {
            assert first.sum() == second.sum();
            this.first = first;
            this.second = second;
        }

        public int sum() {
            return first.sum();
        }

        @Override
        public String toString() {
            return "RamanujanNumber{" +
                    "first=" + first +
                    ", second=" + second +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            RamanujanNumber that = (RamanujanNumber) o;
            return (Objects.equals(first, that.first) && Objects.equals(second, that.second)) ||
                    (Objects.equals(first, that.second) && Objects.equals(second, that.first));
        }

        @Override
        public int hashCode() {
            return Objects.hash(sum());
        }
    }

    /**
     * Finds all Ramanujan numbers up to n, such that a^3 + b^3 = c^3 + d^3 for a, b, c, d integers > 0
     */
    public Set<RamanujanNumber> find(int n) {
        final Set<RamanujanNumber> answer = new HashSet<>();
        final Map<Integer, CubeSum> sumsFound = new HashMap<>();

        for (int i = 1; i < Math.cbrt(n); i++) {
            for (int j = i; j < Math.cbrt(n); j++) {
                final CubeSum cubeSum = new CubeSum(i, j);
                if (sumsFound.containsKey(cubeSum.sum())) {
                    final CubeSum other = sumsFound.get(cubeSum.sum());
                    final RamanujanNumber ramanujanNumber = new RamanujanNumber(cubeSum, other);
                    answer.add(ramanujanNumber);
                    logger.info("Found: {}", ramanujanNumber);
                } else {
                    sumsFound.put(cubeSum.sum(), cubeSum);
                }
            }
        }
        logger.info("In order: {}", Joiner.on(", ").join(answer.stream().mapToInt(RamanujanNumber::sum).sorted().iterator()));
        return answer;
    }
}
