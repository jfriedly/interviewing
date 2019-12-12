package com.jfriedly.interviewing.practice.tadm2e.tsp;

import com.esri.core.geometry.MultiPoint;
import com.esri.core.geometry.Polyline;

/**
 * Solves an instance of the Travelling Salesman Problem (Skiena section 16.4, page 533)
 *
 * Given an input graph as a set of points, the method below returns a tour of them (an order in which they can be
 * visited).  The order is *not* guaranteed to be optimal, but implementations may use heuristics to attempt to find
 * tours with shorter total distances.
 *
 * Note:  this solver interface only solves 2-D *geometric* instances:  ones where the graph is provided as a set of
 * points in two dimensional Euclidean space.  These all satisfy the triangle inequality, that the distance between
 * points *i* and *k* <= the sum of the distance between points *i* and *j*, and the distance between points *j*
 * and *k*.  The Euclidean distance function is also symmetric.
 *
 * Note to self:  I'm not terribly impressed by Esri's geometry library.  The primitive data structures aren't nearly
 * flexible enough, and the primitive operations aren't always guaranteed to return correct results.  Try out JTS
 * next time you need a geometry library.
 */
public interface TSPSolver {

    /**
     * Finds a tour in the graph.  The tour is not guaranteed to be the shortest tour possible.
     *
     * @param input A collection of points in 2-D space.  Note:  {@link MultiPoint}s are actually ordered, so they
     *              implicitly define a tour, but library documentation seems to indicate that this is the best
     *              data structure to use to represent a collection of points, regardless of whether or not they're
     *              ordered.
     * @return A {@link Polyline} containing one line that passes through all of the input points.  Note:
     *         {@link Polyline} is a container for one or more lines, but library documentation seems to indicate
     *         that this is the best data structure to use to represent a single line.  Although {@link MultiPoint}s
     *         are in fact ordered and therefore define a line through each point, it appears that the library
     *         authors intend for users to use {@link Polyline} for this.
     */
    Polyline findTour(MultiPoint input);
}
