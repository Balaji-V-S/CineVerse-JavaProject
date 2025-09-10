// File: src/main/java/com/cineverse/service/ReviewService.java
        package com.cineverse.service;

import com.cineverse.dao.*;
import com.cineverse.entity.*;
import com.cineverse.exception.*;
import com.cineverse.util.JPAUtil;
import jakarta.persistence.*;

public class ReviewService {
    private final ReviewDao reviewDao = new ReviewDaoImpl();
    private final UserDao userDao = new UserDaoImpl();
    private final MovieDao movieDao = new MovieDaoImpl();
    public Review addReview(long customerId, long movieId, String comment, int rating) {
        if (rating < 1 || rating > 5) throw new InvalidRatingException("Rating must be between 1 and 5.");
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Customer customer = (Customer) userDao.findById(customerId, em).orElseThrow(() -> new RuntimeException("Customer not found."));
            Movie movie = movieDao.findById(movieId, em).orElseThrow(() -> new RuntimeException("Movie not found."));
            boolean alreadyReviewed = movie.getReviews().stream().anyMatch(r -> r.getUser().getUserId().equals(customerId));
            if(alreadyReviewed) throw new DuplicateReviewException("You have already reviewed this movie.");

            Review review = new Review();
            review.setUser(customer);
            review.setMovie(movie);
            review.setComment(comment);
            review.setRating(rating);
            reviewDao.save(review, em);

            double newAverage = em.createQuery("SELECT AVG(r.rating) FROM Review r WHERE r.movie.movieId = :movieId", Double.class)
                    .setParameter("movieId", movieId)
                    .getSingleResult();
            movie.setOverallRating(Math.round(newAverage * 10.0) / 10.0);
            movieDao.update(movie, em);

            tx.commit();
            return review;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
