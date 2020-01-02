package com.jfriedly.interviewing.practice.tadm2e.cycledetection;

import java.util.Objects;

public class ListNode {
    private final int id;
    private ListNode next;

    public ListNode(int id) {
        this.id = id;
    }

    public void setNext(ListNode next) {
        this.next = next;
    }

    public ListNode getNext() {
        return next;
    }

    @Override
    public String toString() {
        return String.format("[%d]", id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListNode listNode = (ListNode) o;
        return id == listNode.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
