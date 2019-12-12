package com.jfriedly.interviewing.practice.tadm2e;

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RamanujanNumberFinderTest {

    // Courtesy of OEIS A001235; Moreau 1898:  http://oeis.org/A001235
    final List<Integer> oeisSeries = Arrays.asList(1729, 4104, 13832, 20683, 32832, 39312, 40033, 46683, 64232, 65728, 110656,
            110808, 134379, 149389, 165464, 171288, 195841, 216027, 216125, 262656, 314496, 320264, 327763, 373464,
            402597, 439101, 443889, 513000, 513856, 515375, 525824, 558441, 593047, 684019, 704977);

    @Test
    public void testStaticInput() {
        final RamanujanNumberFinder finder = new RamanujanNumberFinder();

        final Set<Integer> actual = finder.find(750000).stream()
                .map(RamanujanNumberFinder.RamanujanNumber::sum)
                .collect(Collectors.toSet());
        final Set<Integer> expected = new HashSet<>(oeisSeries);

        Assertions.assertThat(actual)
                .isEqualTo(expected);
    }
}
