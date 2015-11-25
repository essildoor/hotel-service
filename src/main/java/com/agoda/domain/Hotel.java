package com.agoda.domain;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Objects;

/**
 * Created by Andrey Kapitonov on 25.11.2015.
 */
public class Hotel {
    private long id;
    private String city;
    private String room;
    private BigDecimal price;

    public Hotel(long id, String city, String room, BigDecimal price) {
        this.id = id;
        this.city = city;
        this.room = room;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        return id == hotel.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Hotel [" +
                "id=" + id +
                ", city='" + city + '\'' +
                ", room='" + room + '\'' +
                ", price=" + price +
                ']';
    }
}
