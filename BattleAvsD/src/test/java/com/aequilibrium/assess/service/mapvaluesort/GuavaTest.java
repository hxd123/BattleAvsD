package com.aequilibrium.assess.service.mapvaluesort;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

public class GuavaTest {

    /**
     * @return a {@link Multimap} whose entries are sorted by descending frequency
     */
    public Multimap<String, String> sortedByDescendingFrequency(Multimap<String, String> multimap) {
        return ImmutableMultimap.<String, String>builder()
                .orderKeysBy(descendingCountOrdering(multimap.keys()))
                .putAll(multimap)
                .build();
    }

    private static Ordering<String> descendingCountOrdering(final Multiset<String> multiset) {
        return new Ordering<String>() {
            @Override
            public int compare(String left, String right) {
                return Ints.compare(multiset.count(left), multiset.count(right));
            }
        };
    }
}
