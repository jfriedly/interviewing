package com.jfriedly.interviewing.practice.tadm2e.cycledetection;

import com.jfriedly.interviewing.practice.common.ListNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * Detects the presence of a cycle in a linked list, returning where it starts and its length if one exists.
 */
public class CycleDetector {

    private static final Logger logger = LoggerFactory.getLogger(CycleDetector.class);

    @Nullable
    public Cycle detect(ListNode head) {
        if (head == null || head.getNext() == null) {
            return null;
        }

        final StringBuilder tortoisePath = new StringBuilder(head.toString());
        ListNode tortoise = head;
        ListNode hare = head;
        int steps = 0;
        // Step the "hare" forward two nodes for every one node stepped forward by the "tortoise".
        // If there's a cycle, the hare is bound to catch up to the tortoise eventually.
        do {
            steps++;
            tortoise = tortoise.getNext();
            tortoisePath.append(" -> ").append(tortoise);

            if (hare.getNext() != null) {
                hare = hare.getNext().getNext();
            } else {
                hare = null;
            }
        } while (!tortoise.equals(hare) && hare != null);

        if (hare == null) {
            logger.debug("No cycle found");
            logger.debug("Tortoise path at exit:  {}", tortoisePath.toString());
            return null;
        }

        // The hare caught up to the tortoise.  That implies that there must have been a cycle.
        logger.debug("Found a cycle at node {} after {} steps", tortoise, steps);
        logger.debug("Tortoise path at exit:  {}", tortoisePath.toString());

        // Find where the cycle starts by starting a new tortoise.  Think of it this way:  when the new tortoise has
        // gone as many steps as the old tortoise has gone now, it will be on the same node as the old tortoise is on
        // now.  The old tortoise, on the other hand, will now be on the same node that the hare was on, because it's
        // gone twice as far as it had gone before.  Since we know those are the same, we know that they'll meet on
        // this node.  However, since the tortoises are also both moving at one node per step, they would have met on
        // the previous node if it were on the cycle.  Inductively, we can see that they'll meet on the first node in
        // the cycle.
        ListNode newTortoise = head;
        int i = 0;
        while (!newTortoise.equals(tortoise)) {
            tortoise = tortoise.getNext();
            newTortoise = newTortoise.getNext();
            i++;
        }
        int indexOfFirstNodeOnCycle = i;
        ListNode firstNodeOnCycle = tortoise;
        logger.debug("First node on cycle was {}", firstNodeOnCycle);

        // Now find the cycle length by counting the number of steps necessary to get back to the current position.
        int j = 0;
        do {
            tortoise = tortoise.getNext();
            j++;
        } while (!tortoise.equals(firstNodeOnCycle));
        int cycleLength = j;
        logger.debug("Cycle length was {}", cycleLength);

        return new Cycle(head, firstNodeOnCycle, indexOfFirstNodeOnCycle, cycleLength);
    }

}
