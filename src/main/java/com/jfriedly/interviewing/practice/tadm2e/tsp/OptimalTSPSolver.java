package com.jfriedly.interviewing.practice.tadm2e.tsp;

import com.esri.core.geometry.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Finds the shortest tour through the input set by testing all n! possible tours.  Executes in O((n + 1)!)
 */
public class OptimalTSPSolver implements TSPSolver {

    private static final Logger logger = LoggerFactory.getLogger(OptimalTSPSolver.class);

    @Override
    public Polyline findTour(MultiPoint input) {
        Polyline shortestTour = new Polyline();
        double shortestTourDistance = Double.MAX_VALUE;

        for (int i = 0; i < input.getPointCount(); i++) {
            final Point start = input.getPoint(i);
            final MultiPoint remaining = (MultiPoint) input.copy();
            remaining.removePoint(i);

            final Polyline shortestSubTour = searchAlltours(remaining, shortestTourDistance, new Polyline(), start);
            final double shortestSubTourDistance = shortestSubTour.calculateLength2D();
            if (shortestSubTourDistance < shortestTourDistance) {
                shortestTourDistance = shortestSubTourDistance;
                shortestTour = (Polyline) shortestSubTour.copy();
            }
        }

        return shortestTour;
    }

    private Polyline searchAlltours(MultiPoint remaining, double shortestTourDistance, Polyline currentTour, Point start) {
        if (remaining.getPointCount() == 0) {
            currentTour.closePathWithLine();
            return currentTour;
        }
        if (currentTour.calculateLength2D() > shortestTourDistance) {
            return currentTour;
        }

        Polyline shortestTour = new Polyline();

        for (int i = 0; i < remaining.getPointCount(); i++) {
            final Point currentPoint = remaining.getPoint(i);

            final Polyline currentSubTour = (Polyline) currentTour.copy();
            if (currentSubTour.isEmpty()) {
                currentSubTour.startPath(start);
            }
            currentSubTour.lineTo(currentPoint);

            final MultiPoint currentRemaining = (MultiPoint) remaining.copy();
            currentRemaining.removePoint(i);

            final Polyline shortestSubTour = searchAlltours(currentRemaining, shortestTourDistance, currentSubTour, start);
            final double shortestSubTourDistance = shortestSubTour.calculateLength2D();
            if (shortestTour.isEmpty()) {
                shortestTour = (Polyline) shortestSubTour.copy();
            }
            if (shortestSubTourDistance < shortestTourDistance) {
                shortestTourDistance = shortestSubTourDistance;
                shortestTour = (Polyline) shortestSubTour.copy();
            }
        }
        return shortestTour;
    }
}
