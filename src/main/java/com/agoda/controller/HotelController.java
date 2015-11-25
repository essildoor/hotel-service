package com.agoda.controller;

import com.agoda.domain.Hotel;
import com.agoda.domain.HotelRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 *
 * Created by Andrey Kapitonov on 25.11.2015.
 */
@RestController
public class HotelController {
    private static final Logger log = LogManager.getLogger(HotelController.class);

    @Autowired
    private HotelRepository hotelRepository;

    @RequestMapping("/hotels")
    public List<Hotel> getHotelsByCity(
            @RequestParam(value = "city", required = true) String city,
            @RequestParam(value = "sortByPrice", defaultValue = "false", required = false) boolean sortByPrice,
            @RequestParam(value = "asc", defaultValue = "false", required = false) boolean asc
    ) {
        List<Hotel> result;
        if (city == null || city.isEmpty()) {
            result = Collections.emptyList();
            log.warn("city must be specified");
        } else {
            result = hotelRepository.getHotelsByCity(city, sortByPrice, asc);
        }

        log.debug(result.size() + " hotels found");
        log.debug(result);

        return result;
    }

}
