package com.cineverse.dao;

import com.cineverse.entity.Review;
import jakarta.persistence.EntityManager;

public class ReviewDao {
    public void save(Review review, EntityManager em) { em.persist(review); }
}