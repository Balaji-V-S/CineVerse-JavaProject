package com.cineverse.dao;

import com.cineverse.entity.Review;
import jakarta.persistence.EntityManager;

public class ReviewDaoImpl implements ReviewDao {
    @Override
    public void save(Review review, EntityManager em) { em.persist(review); }
}