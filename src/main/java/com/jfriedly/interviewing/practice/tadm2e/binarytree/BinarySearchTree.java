package com.jfriedly.interviewing.practice.tadm2e.binarytree;

import com.google.common.base.Preconditions;
import com.jfriedly.interviewing.practice.common.ListNode;
import com.jfriedly.interviewing.practice.common.TreeNode;

/**
 * BST used to answer Skeiner problem 3-22, page 101.  See {@link #toLinkedList()} below.
 */
public class BinarySearchTree {

    private final TreeNode root;

    public BinarySearchTree() {
        this.root = new TreeNode(null);
    }

    public boolean search(Integer value) {
        Preconditions.checkNotNull(value);
        return searchChildren(value, root).getValue().compareTo(value) == 0;
    }

    /**
     * Returns true if the node was inserted, or false if it was already present in the tree.
     */
    public boolean insert(Integer value) {
        Preconditions.checkNotNull(value);
        if (root.getValue() == null) {
            root.setValue(value);
            return true;
        }

        final TreeNode nodeOrParent = searchChildren(value, root);
        final int comparison = value.compareTo(nodeOrParent.getValue());
        if (comparison == 0) {
            // node already exists in the tree
            return false;
        } else if (comparison < 0) {
            nodeOrParent.setLeft(new TreeNode(value));
        } else {
            nodeOrParent.setRight(new TreeNode(value));
        }
        return true;
    }

    /**
     * If this were implemented, I'd make it return true if the node was deleted, or false if it wasn't in the tree
     * already.  Algorithm for BST deletion is:  1) search for the node; 2a) if the node has no children, delete it;
     * 2b) if the node has one child, promote that child, overwriting the node; 2c) if the node has two children, find
     * its left-most right child, swap the two, and then follow rule 2a) or 2b) appropriately.
     */
    public boolean delete(Integer value) {
        throw new UnsupportedOperationException("Problem didn't require delete support, so it's not implemented");
    }

    /**
     * Converts the BST to a linked list using in-order traversal (sorted order).  Skeiner problem 3-22, page 101.
     */
    public ListNode toLinkedList() {
        if (root.getValue() == null) {
            return null;
        }
        // This isn't the real head of the linked list, but we won't find the real head until we traverse the tree.
        // To get started, create a fake "pseudo head" and pass that to the recursive traversal method, then we'll
        // skip past the fake head later.
        final ListNode pseudoHead = new ListNode(-1);
        buildInOrderLinkedList(pseudoHead, root);
        return pseudoHead.getNext();
    }

    @Override
    public String toString() {
        return "BinarySearchTree{" +
                "root=" + root +
                '}';
    }

    private TreeNode searchChildren(Integer value, TreeNode current) {
        final int comparison = value.compareTo(current.getValue());
        if (comparison == 0) {
            return current;
        } else if (comparison < 0) {
            if (current.getLeft() == null) {
                return current;
            }
            return searchChildren(value, current.getLeft());
        } else {
            if (current.getRight() == null) {
                return current;
            }
            return searchChildren(value, current.getRight());
        }
    }

    // TODO(jfriedly):  an inorder traversal method may be reusable.  Maybe it should be a method on TreeNode?
    private ListNode buildInOrderLinkedList(ListNode tail, TreeNode current) {
        if (current == null) {
            return tail;
        }

        tail = buildInOrderLinkedList(tail, current.getLeft());
        tail.setNext(new ListNode(current.getValue()));
        tail = tail.getNext();
        return buildInOrderLinkedList(tail, current.getRight());
    }
}
