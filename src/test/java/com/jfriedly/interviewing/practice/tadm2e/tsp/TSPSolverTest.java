package com.jfriedly.interviewing.practice.tadm2e.tsp;

import com.esri.core.geometry.*;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Random;

public class TSPSolverTest {

    private static final Logger logger = LoggerFactory.getLogger(TSPSolverTest.class);
    private static final Random RANDOM = new Random();
    // I don't know what this should be, so I'm just using the first one
    private static final SpatialReference SPATIAL_REFERENCE = SpatialReference.create(1);

    @DataProvider
    public Object[][] staticInputProvider() {
        // There is only one (unique) tour for this input:  a right triangle.
        final MultiPoint trivialInput = new MultiPoint();
        trivialInput.add(0, 0);
        trivialInput.add(0, 1);
        trivialInput.add(1, 0);

        // Example input derived from Skeiner section 1.1, page 5.  This is basically a clock face
        final MultiPoint optimalInput = new MultiPoint();
        optimalInput.add(3, 10);
        optimalInput.add(5, 8);
        optimalInput.add(5, 4);
        optimalInput.add(3, 2);
        optimalInput.add(-3, 2);
        optimalInput.add(-5, 4);
        optimalInput.add(-5, 8);
        optimalInput.add(-3, 10);

        // Example input from Skeiner section 1.1, page 7
        final MultiPoint nearestNeighborPathologicalInput = new MultiPoint();
        nearestNeighborPathologicalInput.add(0, 0);
        nearestNeighborPathologicalInput.add(0, 1);
        nearestNeighborPathologicalInput.add(0, -1);
        nearestNeighborPathologicalInput.add(0, 3);
        nearestNeighborPathologicalInput.add(0, -5);
        nearestNeighborPathologicalInput.add(0, 11);
        nearestNeighborPathologicalInput.add(0, -21);

        // Example input from Skeiner section 1.1, page 8
        final MultiPoint closestPairPathologicalInput = new MultiPoint();
        closestPairPathologicalInput.add(0, 0);
        closestPairPathologicalInput.add(0, 0.9);
        closestPairPathologicalInput.add(1.1, 0.9);
        closestPairPathologicalInput.add(2.2, 0.9);
        closestPairPathologicalInput.add(2.2, 0);
        closestPairPathologicalInput.add(1.1, 0);

        return new Object[][] {
                { new NearestNeighborTSPSolver(), trivialInput, 3.414213562373095 },
                { new NearestNeighborTSPSolver(), optimalInput, 31.31370849898476 },
                { new NearestNeighborTSPSolver(), nearestNeighborPathologicalInput, 84 },
                { new NearestNeighborTSPSolver(), closestPairPathologicalInput, 7.276972864800943 },

                { new ClosestPairTSPSolver(), trivialInput, 3.414213562373095 },
                { new ClosestPairTSPSolver(), optimalInput, 31.31370849898476 },
                { new ClosestPairTSPSolver(), nearestNeighborPathologicalInput, 64 },
                { new ClosestPairTSPSolver(), closestPairPathologicalInput, 7.276972864800943 },

                { new OptimalTSPSolver(), trivialInput, 3.414213562373095 },
                { new OptimalTSPSolver(), optimalInput, 31.31370849898476 },
                { new OptimalTSPSolver(), nearestNeighborPathologicalInput, 64 },
                { new OptimalTSPSolver(), closestPairPathologicalInput, 6.2 },
        };
    }

    @DataProvider
    public Object[][] implProvider() {
        return new Object[][] {
                { new OptimalTSPSolver(), new NearestNeighborTSPSolver() },
                { new OptimalTSPSolver(), new ClosestPairTSPSolver() },
        };
    }

    @Test(dataProvider = "staticInputProvider")
    public void testStaticInputs(TSPSolver solver, MultiPoint input, double expectedDistance) {
        final Polyline tour = solver.findTour(input);
        final double distance = tour.calculateLength2D();
        logger.info("{} tour was {}: {}", solver.getClass().getSimpleName(), distance, tour);
        Assertions.assertThat(distance)
                .isEqualTo(expectedDistance);
    }

    @Test(dataProvider = "implProvider", invocationCount = 10)
    public void testRandomInputsAgainstOptimalTSP(TSPSolver referenceSolver, TSPSolver testSolver) {
        final MultiPoint input = new MultiPoint();
        int i = 0;
        while (i < 8) {
            final Point current = new Point(RANDOM.nextInt(10), RANDOM.nextInt(10));
            if (OperatorContains.local().execute(input, current, SPATIAL_REFERENCE, null)) {
                // Skip points that are already in the graph
                continue;
            }
            input.add(current);
            i++;
        }
        logger.debug("Random input:  {}", input);

        final Polyline actualTour = testSolver.findTour(input);
        final Polyline referenceTour = referenceSolver.findTour(input);
        logger.info("{} test tour {}:  {}", testSolver.getClass().getSimpleName(), actualTour.calculateLength2D(), actualTour);
        logger.info("{} reference tour {}:  {}", referenceSolver.getClass().getSimpleName(), referenceTour.calculateLength2D(), referenceTour);
        Assertions.assertThat(actualTour.calculateLength2D())
                .isGreaterThanOrEqualTo(referenceTour.calculateLength2D());
    }
}
