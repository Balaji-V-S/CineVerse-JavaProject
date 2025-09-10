package com.cineverse.dao;

import com.cineverse.entity.Booking;
import jakarta.persistence.EntityManager;

public class BookingDaoImpl implements BookingDao {
    @Override
    public void save(Booking booking, EntityManager em) { em.persist(booking); }
}