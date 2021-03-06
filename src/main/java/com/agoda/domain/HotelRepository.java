package com.agoda.domain;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

/**
 *
 * Created by Andrey Kapitonov on 25.11.2015.
 */
@Component
public class HotelRepository {
    private static final Logger log = LogManager.getLogger(HotelRepository.class);

    @Value("${hotel.db.file.name}")
    private String hotelDbFileName;

    @Value("${hotel.db.file.extension}")
    private String hotelDbFileExtension;

    private Map<Long, Hotel> storage;
    private ReentrantLock lock;

    @PostConstruct
    public void initialize() {
        if (hotelDbFileName == null
                || hotelDbFileName.isEmpty()
                || hotelDbFileExtension == null
                || hotelDbFileExtension.isEmpty())
            throw new IllegalArgumentException("check parameters: hotelDbFileName=" + hotelDbFileName
                    + ", hotelDbFileExtension=" + hotelDbFileExtension);

        storage = new HashMap<>();
        lock = new ReentrantLock();

        final String fileName = hotelDbFileName + "." + hotelDbFileExtension;
        Reader in;
        Resource dbFile = new ClassPathResource(fileName);

        try {
            in = new InputStreamReader(dbFile.getInputStream());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withHeader().parse(in);
            for (CSVRecord record : records) {
                long id = -1L;
                if (record.get("HOTELID") != null) {
                    id = Long.parseLong(record.get("HOTELID"));
                }
                String city = record.get("CITY");
                String room = record.get("ROOM");
                BigDecimal price = null;
                if (record.get("PRICE") != null) {
                    price = new BigDecimal(record.get("PRICE"));
                }

                if (id == -1
                        || (city == null || city.isEmpty())
                        || (room == null || room.isEmpty())
                        || price == null) {
                    log.warn("read bad record: [id=" + id + ", city=" + city + ", room=" + room + ", price=" + price);
                } else {
                    Hotel hotel = new Hotel(id, city, room, price);
                    storage.put(id, hotel);

                    log.debug(hotel + " was added to repository");
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        log.debug(storage.size() + " records were read from file");
    }

    /**
     *
     * @return number of records in this repository
     */
    public int size() {
        int result = 0;
        lock.lock();
        try {
            result = storage.size();
        } finally {
            lock.unlock();
        }
        return result;
    }

    /**
     * Returns list of hotels in specified city, optionally sorted by price
     * @param city city to filter hotels
     * @param sortByPrice sort by price
     * @param asc sorting order
     * @return list of hotels
     */
    public List<Hotel> getHotelsByCity(String city, boolean sortByPrice, boolean asc) {
        if (city == null || city.isEmpty()) throw new IllegalArgumentException("city must be specified!");
        List<Hotel> result;

        lock.lock();
        try {
            //get hotels by city
            result = storage.values().stream().filter(hotel -> city.equals(hotel.getCity())).collect(Collectors.toList());
            //sort if needed
            if (sortByPrice) {
                if (asc) result = result.stream().sorted(comparing(Hotel::getPrice)).collect(Collectors.toList());
                else result = result.stream().sorted(comparing(Hotel::getPrice, reverseOrder())).collect(Collectors.toList());
            }
        } finally {
            lock.unlock();
        }

        return result == null ? Collections.emptyList() : result;
    }
}
