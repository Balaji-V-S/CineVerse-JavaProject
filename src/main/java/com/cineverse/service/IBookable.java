package com.cineverse.service;

import com.cineverse.entity.Booking;

public interface IBookable {
    Booking bookTickets(long customerId, long showTimeId, int seatCount);
}