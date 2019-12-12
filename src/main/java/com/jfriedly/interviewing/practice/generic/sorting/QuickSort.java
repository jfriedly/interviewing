package com.jfriedly.interviewing.practice.generic.sorting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class QuickSort implements Sorter {

    private static final Logger logger = LoggerFactory.getLogger(QuickSort.class);

    @Override
    public void sort(int[] arr) {
        quicksort(arr, -1, 0, arr.length - 1);
    }

    @Override
    public int[] smallestK(int[] arr, int k) {
        if (k == 0) {
            return new int[]{};
        }
        quicksort(arr, k, 0, arr.length - 1);
        return Arrays.copyOf(arr, k);
    }

    private void quicksort(int[] arr, int k, int start, int end) {
        logger.debug("entering with args {}, {}, {}", arr, start, end);
        if (end <= start) {
            logger.debug("exiting because {} <= {}", start, end);
            return;
        }
        if (k != -1 && start >= k) {
            logger.debug("exiting because start >= k");
            return;
        }
        final int mid = arr[end];
        int smaller = start;
        int larger = end;
        // When this while loop completes, all of the elements smaller than mid will be at the beginning, and all
        // of the elements larger than mid will be at the end, with the exception of mid itself, which will be
        // swapped last.
        while (smaller < larger) {
            if (arr[smaller] < mid) {
                smaller++;
                continue;
            }
            if (arr[larger] >= mid) {
                larger--;
                continue;
            }
            swap(arr, smaller, larger);

        }
        swap(arr, larger, end);

        logger.debug("arr before recursive calls:  {}", arr);
        quicksort(arr, k, start, larger - 1);
        quicksort(arr, k, larger + 1, end);
        logger.debug("exiting sorted {}, {}, {}", arr, start, end);
    }

}
