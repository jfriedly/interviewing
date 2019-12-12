package com.jfriedly.interviewing.practice.clrs.maxsubarray;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Objects;

public class Subarray {

    private final int[] enclosingArray;
    private final int start;
    private final int end;
    private Integer sum;

    /**
     * Construct a subarray representation of the enclosing array, computing the sum here.
     * @param enclosingArray array that holds the real values
     * @param start          inclusive start index of the subarray within enclosingArray
     * @param end            exclusive end index of the subarray within enclosingArray (may equal enclosingArray.length)
     */
    public Subarray(int[] enclosingArray, int start, int end) {
        Preconditions.checkArgument(start < enclosingArray.length);
        Preconditions.checkArgument(end <= enclosingArray.length);
        Preconditions.checkArgument(end > start);
        this.enclosingArray = enclosingArray;
        this.start = start;
        this.end = end;
        this.sum = null;
    }

    /**
     * Construct a subarray representation of the enclosing array, using a sum specified by the caller.
     */
    public Subarray(int[] enclosingArray, int start, int end, int sum) {
        Preconditions.checkArgument(start < enclosingArray.length);
        Preconditions.checkArgument(end <= enclosingArray.length);
        Preconditions.checkArgument(end > start);
        this.enclosingArray = enclosingArray;
        this.start = start;
        this.end = end;
        this.sum = sum;
    }

    private static int sum(int[] enclosingArray, int start, int end) {
        int sum = 0;
        for (int i = start; i < end; i++) {
            sum += enclosingArray[i];
        }
        return sum;
    }

    public int[] getEnclosingArray() {
        return enclosingArray;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getSum() {
        if (sum == null) {
            sum = sum(enclosingArray, start, end);
        }
        return sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subarray subarray = (Subarray) o;
        return getSum() == subarray.getSum() && enclosingArray == subarray.enclosingArray;

    }

    @Override
    public int hashCode() {
        int result = Objects.hash(start, end, sum);
        result = 31 * result + Arrays.hashCode(enclosingArray);
        return result;
    }

    @Override
    public String toString() {
        return "Subarray{" +
                "enclosingArray=" + Arrays.toString(enclosingArray) +
                ", start=" + start +
                ", end=" + end +
                ", sum=" + sum +
                '}';
    }
}
