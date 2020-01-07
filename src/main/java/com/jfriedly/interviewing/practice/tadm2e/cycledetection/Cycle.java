package com.jfriedly.interviewing.practice.tadm2e.cycledetection;

import com.jfriedly.interviewing.practice.generic.ListNode;

public class Cycle {

    private final ListNode head;
    private final ListNode firstNodeOnCycle;
    private final int indexOfFirstNodeOnCycle;
    private final int cycleLength;

    public Cycle(ListNode head, ListNode firstNodeOnCycle, int indexOfFirstNodeOnCycle, int cycleLength) {
        this.head = head;
        this.firstNodeOnCycle = firstNodeOnCycle;
        this.indexOfFirstNodeOnCycle = indexOfFirstNodeOnCycle;
        this.cycleLength = cycleLength;
    }

    public ListNode getHead() {
        return head;
    }

    public ListNode getFirstNodeOnCycle() {
        return firstNodeOnCycle;
    }

    public int getIndexOfFirstNodeOnCycle() {
        return indexOfFirstNodeOnCycle;
    }

    public int getCycleLength() {
        return cycleLength;
    }
}
