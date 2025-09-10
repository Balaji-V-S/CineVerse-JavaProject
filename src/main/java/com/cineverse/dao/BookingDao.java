package com.cineverse.dao;

import com.cineverse.entity.Booking;
import jakarta.persistence.EntityManager;

public interface BookingDao {
    void save(Booking booking, EntityManager em);
}