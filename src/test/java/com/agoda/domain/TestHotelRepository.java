package com.agoda.domain;

import com.agoda.HotelServiceMain;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.*;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsEqual.*;
import static org.junit.Assert.*;

/**
 *
 * Created by Andrey Kapitonov on 25.11.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(HotelServiceMain.class)
public class TestHotelRepository {

    @Autowired
    private HotelRepository repository;

    @Test
    public void testRepositoryInit() throws Exception {
        int hotelsCountInDb = 26;
        assertThat(hotelsCountInDb, is(equalTo(repository.size())));
    }

    @Test
    public void testGetHotelsByCity() throws Exception {
        final String city = "Bangkok";
        List<Hotel> expectedUnsorted = Arrays.asList(
                new Hotel(1, "Bangkok", "Deluxe", new BigDecimal(1000)),
                new Hotel(6, "Bangkok", "Superior", new BigDecimal(2000)),
                new Hotel(8, "Bangkok", "Superior", new BigDecimal(2400)),
                new Hotel(11, "Bangkok", "Deluxe", new BigDecimal(60)),
                new Hotel(14, "Bangkok", "Sweet Suite", new BigDecimal(25000)),
                new Hotel(15, "Bangkok", "Deluxe", new BigDecimal(900)),
                new Hotel(18, "Bangkok", "Sweet Suite", new BigDecimal(5300))
        );
        List<Hotel> expectedSortedByPriceAsc = expectedUnsorted
                .stream()
                .sorted(Comparator.comparing(Hotel::getPrice))
                .collect(Collectors.toList());
        List<Hotel> expectedSortedByPriceDesc = expectedUnsorted
                .stream()
                .sorted(Comparator.comparing(Hotel::getPrice, Comparator.reverseOrder()))
                .collect(Collectors.toList());

        assertThat(expectedUnsorted, containsInAnyOrder(repository.getHotelsByCity(city, false, false).toArray()));
        assertThat(expectedSortedByPriceAsc, IsIterableContainingInOrder.contains(repository.getHotelsByCity(city, true, true).toArray()));
        assertThat(expectedSortedByPriceDesc, IsIterableContainingInOrder.contains(repository.getHotelsByCity(city, true, false).toArray()));
    }
}
