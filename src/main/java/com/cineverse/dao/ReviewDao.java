package com.cineverse.dao;

import com.cineverse.entity.Review;
import jakarta.persistence.EntityManager;

public interface ReviewDao {
    void save(Review review, EntityManager em);
}