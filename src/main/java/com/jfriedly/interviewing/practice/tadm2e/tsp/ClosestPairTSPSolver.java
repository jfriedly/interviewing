package com.jfriedly.interviewing.practice.tadm2e.tsp;

import com.esri.core.geometry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Finds a sub-optimal tour through the input set by repeatedly selecting the closest pair of unconnected points
 *
 * The tour returned is *not* guaranteed to be the shortest one.  Pathological cases exist.  See the test case.
 */
public class ClosestPairTSPSolver implements TSPSolver {

    private static final Logger logger = LoggerFactory.getLogger(ClosestPairTSPSolver.class);
    // I don't know what this should be, so I'm just using the first one
    private static final SpatialReference SPATIAL_REFERENCE = SpatialReference.create(1);

    @Override
    public Polyline findTour(MultiPoint input) {
        final Polyline tour = new Polyline();
        final Polyline tmpSegments = new Polyline();
        if (input.getPointCount() == 0) {
            return tour;
        } else if (input.getPointCount() == 1) {
            tour.startPath(input.getPoint(0));
            return tour;
        }

        while (tour.getPointCount() < input.getPointCount()) {
            logger.debug("Current tour:  {}", tour);
            logger.debug("tmpSegments: {}", tmpSegments);
            final Segment closestPairSegment = findClosestPair(input, tour, tmpSegments);
            tmpSegments.addSegment(closestPairSegment, true);
            logger.debug("tmpSegments after finding closest pair: {}", tmpSegments);
            fixUpTmpSegments(tmpSegments);
            logger.debug("tmpSegments after fixing up: {}", tmpSegments);
            addUsableSegmentsToTour(tour, tmpSegments);
            logger.debug("tour after adding usable segments: {}", tour);
            logger.debug("tmpSegments after adding usable segments: {}", tmpSegments);
        }

        tour.closePathWithLine();
        logger.debug("Final tmpSegments was {}", tmpSegments);
        return tour;
    }


    /**
     * Find the closest pair of points not in the tour or in tmpSegments.  Return a line segment between them.
     */
    private Segment findClosestPair(MultiPoint input, Polyline tour, Polyline tmpSegments) {
        final MultiPoint tourBoundary = (MultiPoint) tour.getBoundary();
        final MultiPoint tmpSegmentsBoundary = (MultiPoint) tmpSegments.getBoundary();
        double minimumDistance = Double.MAX_VALUE;
        Line minimumLine = null;

        for (int i = 0; i < input.getPointCount(); i++) {
            final Point first = input.getPoint(i);
            logger.trace("findClosestPair selected first point {}", first);
            if (OperatorContains.local().execute(tour, first, SPATIAL_REFERENCE, null)) {
                continue;
            }
            if (OperatorContains.local().execute(tmpSegments, first, SPATIAL_REFERENCE, null)) {
                continue;
            }
            final boolean firstOnTourBoundary = OperatorContains.local()
                    .execute(tourBoundary, first, SPATIAL_REFERENCE, null);
            final boolean firstOnTmpSegmentsBoundary = OperatorContains.local()
                    .execute(tmpSegmentsBoundary, first, SPATIAL_REFERENCE, null);

            SECOND:
            for (int j = 0; j < input.getPointCount(); j++) {
                if (i == j) {
                    continue;
                }
                final Point second = input.getPoint(j);
                logger.trace("findClosestPair selected second point {}", second);
                final double distance = OperatorDistance.local().execute(first, second, null);
                if (distance >= minimumDistance) {
                    logger.trace("Continuing because distance {} >= {}", distance, minimumDistance);
                    continue;
                }
                if (OperatorContains.local().execute(tour, second, SPATIAL_REFERENCE, null)) {
                    continue;
                }
                if (OperatorContains.local().execute(tmpSegments, second, SPATIAL_REFERENCE, null)) {
                    continue;
                }
                final boolean secondOnTourBoundary = OperatorContains.local()
                        .execute(tourBoundary, second, SPATIAL_REFERENCE, null);
                final boolean secondOnTmpSegmentsBoundary = OperatorContains.local()
                        .execute(tmpSegmentsBoundary, second, SPATIAL_REFERENCE, null);
                if (firstOnTourBoundary && secondOnTourBoundary) {
                    // If they're both on the tour boundary, then the current line segment would close the tour.
                    // Skip it.
                    logger.trace("Continuing because both points are on the tour boundary");
                    continue;
                }
                if (firstOnTmpSegmentsBoundary && secondOnTmpSegmentsBoundary) {
                    logger.trace("Both points are on the tmpSegments boundary; checking paths");
                    // If they're both on the tmp segments boundary, then either:
                    //  1)  The line segment already exists in tmpSegments.  It will need to be skipped
                    //  2)  The line segment would connect to disconnected segments.  This is fine.  fixUpTmpSegments()
                    //      below will merge them into one.
                    //  3)  The line segment would close path in tmpSegments, creating a polygon.  That can't happen;
                    //      the tour can't contain polygons.  It will need to be skipped.
                    // So, the loop below checks each path and if it finds that both endpoints are on the *same* path
                    // (options #1 or #3 above), the line segment is skipped.
                    for (int k = 0; k < tmpSegments.getPathCount(); k++) {
                        final Point pathStart = tmpSegments.getPoint(tmpSegments.getPathStart(k));
                        final Point pathEnd = tmpSegments.getPoint(tmpSegments.getPathEnd(k) - 1);
                        logger.trace("{}th path has start {} and end {}", k, pathStart, pathEnd);
                        if ((first.equals(pathStart) && second.equals(pathEnd)) || (first.equals(pathEnd) && second.equals(pathStart))) {
                            logger.trace("Continuing because the points would close a path in tmpSegments");
                            continue SECOND;
                        }
                    }
                }

                logger.debug("Setting new minimum distance to {} for line segment {} to {}", distance, first, second);
                minimumDistance = distance;
                minimumLine = new Line(first.getX(), first.getY(), second.getX(), second.getY());
            }
        }

        return minimumLine;
    }

    /**
     * "Fix up" the tour by adding any usable segments to one end of the tour or the other.
     */
    private void addUsableSegmentsToTour(Polyline tour, Polyline tmpSegments) {
        if (tour.isEmpty() && tmpSegments.getPathCount() == 1) {
            tour.addPath(tmpSegments, 0, true);
            tmpSegments.removePath(0);
            return;
        }

        MultiPoint intersection = (MultiPoint) OperatorIntersection.local()
                .execute(tour.getBoundary(), tmpSegments.getBoundary(), SPATIAL_REFERENCE, null);

        while (!intersection.isEmpty()) {
            int intersectingPathIndex;
            for (intersectingPathIndex = 0; intersectingPathIndex < tmpSegments.getPathCount(); intersectingPathIndex++) {
                final boolean pathsMerged = mergePaths(tour, tour, 0, tmpSegments, intersectingPathIndex);
                if (pathsMerged) {
                    tour.removePath(0);
                    tmpSegments.removePath(intersectingPathIndex);
                    break;
                }
            }

            // Update intersection to prepare for next loop round
            intersection = (MultiPoint) OperatorIntersection.local()
                    .execute(tour.getBoundary(), tmpSegments.getBoundary(), SPATIAL_REFERENCE, null);
        }
    }

    /**
     * Collapse any connecting segments in tmpSegments into a single segment.  This makes it easy for
     * {@link #findClosestPair(MultiPoint, Polyline, Polyline)} to avoid accidentally constructing a polygon in
     * tmpSegments.
     */
    private void fixUpTmpSegments(Polyline tmpSegments) {
        boolean connectingSegmentsFound;
        OUTER:
        do {
            connectingSegmentsFound = false;
            for (int i = 0; i < tmpSegments.getPathCount(); i++) {
                for (int j = 0; j < tmpSegments.getPathCount(); j++) {
                    if (i == j) {
                        continue;
                    }
                    connectingSegmentsFound = mergePaths(tmpSegments, tmpSegments, i, tmpSegments, j);
                    if (connectingSegmentsFound) {
                        // create a new path including all the segments on both paths i and j, then delete paths i and j
                        // tmpSegments acts as an array, so in order to remove two elements from the array, you
                        // have to make sure that you delete the larger one first.  Otherwise everything would shift
                        // over to the left >.<
                        if (i > j) {
                            tmpSegments.removePath(i);
                            tmpSegments.removePath(j);
                        } else {
                            tmpSegments.removePath(j);
                            tmpSegments.removePath(i);
                        }
                        continue OUTER;
                    }
                }
            }
        } while (connectingSegmentsFound);
    }

    /**
     * Merge paths from the source {@link Polyline}s into a new path in the destination Polyline.
     *
     * Note:  callers must delete any redundant paths later.  This method only creates a new merged path.
     */
    private boolean mergePaths(Polyline destination, Polyline src1, int src1PathIndex, Polyline src2, int src2PathIndex) {
        Point firstStart = src1.getPoint(src1.getPathStart(src1PathIndex));
        final Point firstEnd = src1.getPoint(src1.getPathEnd(src1PathIndex) - 1);
        final Point secondStart = src2.getPoint(src2.getPathStart(src2PathIndex));
        final Point secondEnd = src2.getPoint(src2.getPathEnd(src2PathIndex) - 1);

        if (firstStart.equals(secondStart) || firstStart.equals(secondEnd) || firstEnd.equals(secondStart) || firstEnd.equals(secondEnd)) {
            if (firstStart.equals(secondStart)) {
                src1.reversePath(src1PathIndex);
                firstStart = src1.getPoint(src1.getPathStart(src1PathIndex));
            }
            if (firstStart.equals(secondEnd)) {
                src1.reversePath(src1PathIndex);
                src2.reversePath(src2PathIndex);
                firstStart = src1.getPoint(src1.getPathStart(src1PathIndex));
            }
            if (firstEnd.equals(secondEnd)) {
                src2.reversePath(src2PathIndex);
            }

            destination.startPath(firstStart);
            for (int k = src1.getPathStart(src1PathIndex) + 1; k < src1.getPathEnd(src1PathIndex); k++) {
                destination.lineTo(src1.getPoint(k));
            }
            for (int k = src2.getPathStart(src2PathIndex) + 1; k < src2.getPathEnd(src2PathIndex); k++) {
                destination.lineTo(src2.getPoint(k));
            }
            return true;
        }
        return false;
    }
}
