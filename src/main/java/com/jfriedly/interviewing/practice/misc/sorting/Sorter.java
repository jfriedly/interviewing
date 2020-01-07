package com.jfriedly.interviewing.practice.misc.sorting;

import java.util.Arrays;

public interface Sorter {

    void sort(int[] array);

    default int[] smallestK(int[] array, int k) {
        sort(array);
        return Arrays.copyOf(array, k);
    }

    default void swap(int[] array, int first, int second) {
        final int temp = array[first];
        array[first] = array[second];
        array[second] = temp;
    }

}
