package com.jfriedly.interviewing.practice.misc.sorting;

public class InsertionSort implements Sorter {

    @Override
    public void sort(int[] array) {
        sortCountingInversions(array);
    }

    /**
     * Sort the array, but also count the number of inversions, as defined by CLRS Problem 2-4
     * Every time insertion sort shifts an element, that's one inversion.
     */
    /* pp */ int sortCountingInversions(int[] array) {
        int inversionCount = 0;
        for (int i = 0; i < array.length; i++) {
            final int key = array[i];
            int j = i;
            while (j > 0 && array[j - 1] > key) {
                array[j] = array[j - 1];
                inversionCount++;
                j--;
            }
            array[j] = key;
        }
        return inversionCount;
    }
}
