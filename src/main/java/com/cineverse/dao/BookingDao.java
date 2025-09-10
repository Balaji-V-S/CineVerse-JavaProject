package com.cineverse.dao;

import com.cineverse.entity.Booking;
import jakarta.persistence.EntityManager;

public class BookingDao {
    public void save(Booking booking, EntityManager em) { em.persist(booking); }
}
