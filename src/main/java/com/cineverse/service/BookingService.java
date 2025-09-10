// File: src/main/java/com/cineverse/service/BookingService.java
package com.cineverse.service;

import com.cineverse.dao.*;
import com.cineverse.entity.*;
import com.cineverse.exception.SeatsUnavailableException;
import com.cineverse.util.JPAUtil;
import jakarta.persistence.*;

public class BookingService implements IBookable {
    private final BookingDao bookingDao = new BookingDaoImpl();
    private final UserDao userDao = new UserDaoImpl();
    private final ShowTimeDao showTimeDao = new ShowTimeDaoImpl();
    @Override
    public Booking bookTickets(long customerId, long showTimeId, int seatCount) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Customer customer = (Customer) userDao.findById(customerId, em).orElseThrow(() -> new RuntimeException("Customer not found."));
            ShowTime showTime = showTimeDao.findById(showTimeId, em).orElseThrow(() -> new RuntimeException("ShowTime not found."));
            if (showTime.getAvailableSeats() < seatCount) throw new SeatsUnavailableException("Not enough seats. Available: " + showTime.getAvailableSeats());
            showTime.setAvailableSeats(showTime.getAvailableSeats() - seatCount);
            Booking booking = new Booking();
            booking.setCustomer(customer);
            booking.setShowTime(showTime);
            booking.setSeatCount(seatCount);
            booking.setStatus("CONFIRMED");
            bookingDao.save(booking, em);
            tx.commit();
            return booking;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}