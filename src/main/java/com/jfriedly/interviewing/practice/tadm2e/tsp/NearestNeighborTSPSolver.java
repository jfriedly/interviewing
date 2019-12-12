package com.jfriedly.interviewing.practice.tadm2e.tsp;

import com.esri.core.geometry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Finds a sub-optimal tour through the input set by repeatedly selecting the nearest neighbor.
 *
 * The tour returned is *not* guaranteed to be the shortest one.  Pathological cases exist.  See the test case.
 */
public class NearestNeighborTSPSolver implements TSPSolver {

    private static final Logger logger = LoggerFactory.getLogger(NearestNeighborTSPSolver.class);
    // I don't know what this should be, so I'm just using the first one
    private static final SpatialReference SPATIAL_REFERENCE = SpatialReference.create(1);

    @Override
    public Polyline findTour(MultiPoint input) {
        final Polyline tour = new Polyline();
        if (input.getPointCount() == 0) {
            return tour;
        }

        final MultiPoint visited = new MultiPoint();
        final Point start = input.getPoint(0);
        tour.startPath(start);
        Point current = start;
        for (int i = 1; i < input.getPointCount(); i++) {
            // Find the nearest neighbor to our current point
            Point nearestNeighbor = null;
            double nearestNeighborDistance = Double.MAX_VALUE;
            for (int j = 1; j < input.getPointCount(); j++) {
                final Point neighbor = input.getPoint(j);
                if (OperatorContains.local().execute(visited, neighbor, SPATIAL_REFERENCE, null)) {
                    continue;
                }
                final double distance = OperatorDistance.local().execute(current, neighbor, null);
                if (distance < nearestNeighborDistance) {
                    nearestNeighborDistance = distance;
                    nearestNeighbor = neighbor;
                }
            }

            // Remove the nearest neighbor and add it to the tour
            visited.add(nearestNeighbor);
            tour.lineTo(nearestNeighbor);
            current = nearestNeighbor;
        }

        tour.closePathWithLine();
        return tour;
    }
}
