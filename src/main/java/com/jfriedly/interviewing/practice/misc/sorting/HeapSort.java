package com.jfriedly.interviewing.practice.misc.sorting;

public class HeapSort implements Sorter {

    @Override
    public void sort(int[] array) {
        final int[] heap = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            minHeapInsert(heap, i, array[i]);
        }
        // Once everything's in the heap, we can get them back out and put them in the array in sorted order
        for (int i = 0; i < array.length; i++) {
            array[i] = minHeapRemoveRoot(heap, array.length - i);
        }
    }

    @Override
    public int[] smallestK(int[] array, int k) {
        final int[] heap = new int[k];
        int heapSize = 0;
        if (k == 0) {
            return heap;
        }

        for (int i = 0; i < array.length; i++) {
            if (heapSize < k) {
                maxHeapInsert(heap, heapSize, array[i]);
                heapSize++;
            } else if (array[i] < heap[0]) {
                maxHeapRemoveRoot(heap, heapSize);
                heapSize--;
                maxHeapInsert(heap, heapSize, array[i]);
                heapSize++;
            }
        }
        return heap;
    }

    /**
     * Insert a value into the min-heap.
     *
     * Since the heap is implicit, the caller must pass in the heap's size prior to insertion.
     *
     * @param heap int[] representing an implicit heap
     * @param size size of the heap before inserting the new value
     * @param value new value to be inserted
     */
    private void minHeapInsert(int[] heap, int size, int value) {
        int indexOfNewValue = size;
        heap[indexOfNewValue] = value;
        while (heap[indexOfNewValue] < heap[parent(indexOfNewValue)]) {
            swap(heap, indexOfNewValue, parent(indexOfNewValue));
            indexOfNewValue = parent(indexOfNewValue);
        }
    }

    /**
     * Insert a value into the max-heap.
     *
     * Since the heap is implicit, the caller must pass in the heap's size prior to insertion.
     *
     * @param heap int[] representing an implicit heap
     * @param size size of the heap before inserting the new value
     * @param value new value to be inserted
     */
    private void maxHeapInsert(int[] heap, int size, int value) {
        int indexOfNewValue = size;
        heap[indexOfNewValue] = value;
        while (heap[indexOfNewValue] > heap[parent(indexOfNewValue)]) {
            swap(heap, indexOfNewValue, parent(indexOfNewValue));
            indexOfNewValue = parent(indexOfNewValue);
        }
    }

    private int heapPeek(int[] heap) {
        return heap[0];
    }

    /**
     * Remove the root of the min-heap, the smallest value in it.
     *
     * Since the heap is implicit, the caller must pass in the heap's size prior to insertion.
     *
     * @param heap int[] representing an implicit heap
     * @param size size of the heap before removing the smallest value
     */
    private int minHeapRemoveRoot(int[] heap, int size) {
        final int minValue = heap[0];
        swap(heap, 0, size - 1);
        int current = 0;
        while ((leftChild(current) < size - 1 && heap[current] > heap[leftChild(current)]) ||
               (rightChild(current) < size - 1 && heap[current] > heap[rightChild(current)])) {
            // If the right child exists, then the left child must exist because of the heap shape property.
            if (rightChild(current) < size - 1) {
                if (heap[leftChild(current)] < heap[rightChild(current)]) {
                    swap(heap, current, leftChild(current));
                    current = leftChild(current);
                } else {
                    swap(heap, current, rightChild(current));
                    current = rightChild(current);
                }
            } else {
                swap(heap, current, leftChild(current));
                current = leftChild(current);
            }
        }

        return minValue;
    }

    /**
     * Remove the root of the max-heap, the largest value in it.
     *
     * Since the heap is implicit, the caller must pass in the heap's size prior to insertion.
     *
     * @param heap int[] representing an implicit heap
     * @param size size of the heap before removing the smallest value
     */
    private int maxHeapRemoveRoot(int[] heap, int size) {
        final int minValue = heap[0];
        swap(heap, 0, size - 1);
        int current = 0;
        while ((leftChild(current) < size - 1 && heap[current] < heap[leftChild(current)]) ||
                (rightChild(current) < size - 1 && heap[current] < heap[rightChild(current)])) {
            // If the right child exists, then the left child must exist because of the heap shape property.
            if (rightChild(current) < size - 1) {
                if (heap[leftChild(current)] > heap[rightChild(current)]) {
                    swap(heap, current, leftChild(current));
                    current = leftChild(current);
                } else {
                    swap(heap, current, rightChild(current));
                    current = rightChild(current);
                }
            } else {
                swap(heap, current, leftChild(current));
                current = leftChild(current);
            }
        }

        return minValue;
    }

    private int leftChild(int index) {
        return index * 2 + 1;
    }

    private int rightChild(int index) {
        return index * 2 + 2;
    }

    private int parent(int index) {
        return (index - 1) / 2;
    }
}
