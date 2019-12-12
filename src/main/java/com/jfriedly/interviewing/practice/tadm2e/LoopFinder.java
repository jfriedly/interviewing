package com.jfriedly.interviewing.practice.tadm2e;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * Finds the start of a loop in a linked list.
 */
public class LoopFinder {

    private static final Logger logger = LoggerFactory.getLogger(LoopFinder.class);

    private final ListNode head;

    private Boolean loopExists = null;
    // literature refers to this value as *v*
    private int initialDiscoveryLength = -1;
    // literature refers to this value as *x_v*
    private ListNode initialDiscovery = null;
    // literature refers to this value as *lambda*
    private int loopLength = -1;
    // literature refers to this value as *mu*
    private int loopStart = -1;

    public LoopFinder(ListNode head) {
        this.head = head;
    }

    public static class ListNode {
        private final int id;
        private ListNode next;

        public ListNode(int id) {
            this.id = id;
        }

        public void setNext(ListNode next) {
            this.next = next;
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

    /**
     * Returns whether or not a loop exists in the linked list.
     */
    public boolean loopExists() {
        if (loopExists == null) {
            loopExists = discoverLoop();
        }
        return loopExists;
    }

    /**
     * Return the length of the loop in the linked list.
     *
     * Throws an exception if there is no loop.  Callers should check {@link #loopExists()} before calling this
     * method unless they are already certain that a loop exists.
     */
    public int loopLength() {
        if (!loopExists()) {
            throw new IllegalStateException("Cannot find loop length when there is no loop");
        }
        if (loopLength == -1) {
            loopLength = loopLengthGivenNodeOnLoop();
        }
        return loopLength;
    }

    /**
     * Return the index of the first element that is on the loop.
     *
     * Throws an exception if there is no loop.  Callers should check {@link #loopExists()} before calling this
     * method unless they are already certain that a loop exists.
     */
    public int loopStart() {
        if (!loopExists()) {
            throw new IllegalStateException("Cannot find loop start when there is no loop");
        }
        if (loopStart == -1) {
            // My initial implementation was a quadratic search, with nodes on how it could be improved to nlog(n)
            // All tests pass with this implementation.
            //loopStart = loopStartQuadraticSearch();

            // After reading https://en.wikipedia.org/wiki/Cycle_detection#Floyd's_Tortoise_and_Hare , I was able
            // to implement Floyd's algorithm.
            loopStart = loopStartFloydsAlgorithm();
        }
        return loopStart;
    }

    /**
     * Finds a loop in the linked list using the tortoise-and-hare method, returning true if one is found, or false
     * if the end of the linked list (a null pointer) is found.
     */
    private boolean discoverLoop() {
        if (head == null) {
            return false;
        }
        if (loopExists != null && loopExists) {
            return true;
        }

        final StringBuilder tortoisePath = new StringBuilder(head.toString());
        int steps = 0;
        ListNode tortoise = head;
        ListNode hare = head;
        // Step the "hare" forward two nodes for every one node stepped forward by the "tortoise".
        // If there's a loop, the hare is bound to catch up to the tortoise eventually.
        while (hare != null) {
            steps++;
            if (tortoise.next != null) {
                tortoise = tortoise.next;
                tortoisePath.append(" -> ").append(tortoise);
            } else {
                // This can only happen on single-element lists, when both pointers hit null simultaneously.  Any list
                // with at least two elements will see hare hit null first and the loop will exit.
                tortoise = null;
                tortoisePath.append(" -> ").append(tortoise);
                break;
            }

            if (hare.next != null) {
                hare = hare.next.next;
            } else {
                hare = null;
            }

            // The hare caught up to the tortoise.  That implies that there must have been a loop.
            if (tortoise.equals(hare)) {
                logger.debug("Found a loop at node {} after {} steps", tortoise, steps);
                logger.debug("Tortoise path at exit:  {}", tortoisePath.toString());
                initialDiscovery = tortoise;
                initialDiscoveryLength = steps;
                return true;
            }
        }
        logger.debug("No loop found");
        logger.debug("Tortoise path at exit:  {}", tortoisePath.toString());
        return false;
    }

    /**
     * Given a node on the loop found by {@link #discoverLoop()}, return the loop's length.
     *
     * Holding the hare still, step the tortoise forward until it meets the hare again.  The number of steps
     * necessary is the loopLength.
     */
    private int loopLengthGivenNodeOnLoop() {
        ListNode tortoise = initialDiscovery;
        int steps = 0;
        do {
            tortoise = tortoise.next;
            steps++;
        } while (!tortoise.equals(initialDiscovery));
        loopLength = steps;
        logger.debug("Loop length was {}", loopLength);
        return loopLength;
    }

    /**
     * Given an initial discovery point found by {@link #discoverLoop()}, return the start of the loop.
     *
     * This method makes use of the fact that the tortoise's path length at the end of {@link #discoverLoop()} is
     * guaranteed to be an integer multiple of the loop length.  A short proof of this fact is below.  Knowing that
     * the tortoise's path length was a multiple of the loop length, you can find where the loop starts by starting
     * a new tortoise back at the beginning.  When the new tortoise reaches the initialDiscoveryLength steps, it will
     * be on the same node as the old tortoise, because the old tortoise has just gone some integer number of loops.
     * However, since they both move one step at a time, that means that if the previous node was on the loop, then
     * they would have met there.  Inductively, we can see that they'll meet at the first node on the loop this way,
     * so the first place they meet must be the start of the loop.
     *
     * Proof that the tortoise's path length is an integer multiple of the loop length:
     *
     * Let *mu* be the number of nodes before the loop starts and let
     * Let *lambda* be the number of nodes on the loop.
     * Let *t* be the tortoise's path length.
     * Let *h* be the hare's path length.
     *
     * By definition, the hare must have gone twice as far as the tortoise, so:
     * h = 2t
     *
     * Let *o* be the offset into the loop at which the hare and tortoise meet, *initialDiscoverLength - mu*.  Then:
     * t = mu + o
     *
     * Let *c* be the number of full cycles that the hare made through the loop before it found the tortoise.  Then:
     * h = mu + c * lambda + o
     *
     * We can solve these three equations for t:
     * o = h - mu - c * lambda
     * t = mu + (h - mu - c * lambda)
     * t = h - c * lambda
     * t = 2t - c * lambda
     * t = c * lambda.
     *
     * This demonstrates that the tortoise's path length is equal to the number of full cycles that the hare made
     * through the loop, which must be divisible by the loop length.
     *
     * This is an implementation of Floyd's algorithm, as described at
     * https://en.wikipedia.org/wiki/Cycle_detection#Floyd's_Tortoise_and_Hare
     */
    private int loopStartFloydsAlgorithm() {
        ListNode tortoise = initialDiscovery;
        ListNode newTortoise = head;
        for (int i = 0; i < initialDiscoveryLength + 1; i++) {
            if (newTortoise.equals(tortoise)) {
                loopStart = i;
                break;
            }
            tortoise = tortoise.next;
            newTortoise = newTortoise.next;
        }
        return loopStart;
    }

    /**
     * Given a node on the loop found by {@link #discoverLoop()} and the loop's length found by {@link #loopLength()},
     * test all of the candidate nodes on which the loop could have started in sequence until the first one on the
     * loop is found.  Return the index of that node.
     *
     * Given the initialDiscovery node, the path length to it, and the loop length, we know that the loop must have
     * started at some index no earlier than initialDiscoveryLength - loopLength because the hare "laps" the tortoise
     * by the time the tortoise can finish its first loop.  (Note:  in general, the hare laps the tortoise *before* it
     * finishes the first loop, but if the entire list is one large loop, i.e. a circular linked list, then we can't
     * discover that until the tortoise has completed an entire lap because the tortoise and the hare of course must
     * have the same starting position.)
     * Treating all the nodes from initialDiscoveryLength - loopLength (inclusive) to initialDiscoveryLength (inclusive)
     * as candidates, we can test each one to see if it's on the loop.  (Note that this is loopLength + 1 candidates
     * due to the special case of a circular linked list above.)  Testing a candidate is simple:  just proceed forward
     * loopLength nodes and check if the new node is equal to the starting node.  The first candidate for which this
     * is true is the first node on the loop.
     *
     * Note that this implementation is O(loopLength^2 * loopStart).  Faster implementations include:
     * - By maintaining a pointer to the first candidate, iteration through the loopStart nodes each time can be
     * skipped, yielding O(loopLength^2 + loopStart).
     * - The candidates can be binary searched by choosing the central one and then proceeding left if it was part
     * of a loop or right if it wasn't.  This reduces the potential time down to
     * O(loopLength * log(loopLength) + loopStart)
     * - Using a clever algebraic trick, testing candidates can be completely skipped.  See
     * {@link #loopStartFloydsAlgorithm()}.
     *
     * This class is my own work.  {@link #loopStartFloydsAlgorithm()} was based on Floyd's algorithm:
     * https://en.wikipedia.org/wiki/Cycle_detection#Floyd's_Tortoise_and_Hare
     */
    private int loopStartQuadraticSearch() {
        if (loopLength == -1) {
            loopLength = loopLengthGivenNodeOnLoop();
        }
        for (int i = initialDiscoveryLength - loopLength; i < initialDiscoveryLength + 1; i++) {
            ListNode candidate = head;
            for (int j = 0; j < i; j++) {
                candidate = candidate.next;
            }
            ListNode candidateSingleStepper = candidate;
            for (int j = 0; j < loopLength; j++) {
                candidateSingleStepper = candidateSingleStepper.next;
            }
            if (candidate.equals(candidateSingleStepper)) {
                loopStart = i;
                return loopStart;
            }
        }
        throw new IllegalStateException("Loop exists, but we couldn't find the loop start.  There's a bug somewhere");
    }

}
