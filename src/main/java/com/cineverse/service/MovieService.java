// File: src/main/java/com/cineverse/service/MovieService.java
package com.cineverse.service;

import com.cineverse.dao.MovieDao;
import com.cineverse.dao.MovieDaoImpl;
import com.cineverse.entity.*;
import com.cineverse.exception.*;
import com.cineverse.util.JPAUtil;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MovieService {
    private final MovieDao movieDao = new MovieDaoImpl();
    public Movie addMovie(User currentUser, String title, String genre, int duration, LocalDate releaseDate, List<ShowTime> showTimes) {
        if (!(currentUser instanceof Admin)) throw new UnauthorizedAccessException("Only admins can add movies.");
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Movie movie = new Movie();
            movie.setTitle(title);
            movie.setGenre(genre);
            movie.setDurationInMinutes(duration);
            movie.setReleaseDate(releaseDate);
            showTimes.forEach(st -> {
                if(st.getStartTime().isBefore(LocalDateTime.now())) throw new InvalidShowTimeException("Cannot add a showtime in the past.");
                st.setMovie(movie);
            });
            movie.setShowTimes(showTimes);
            movieDao.save(movie, em);
            tx.commit();
            return movie;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
    public List<Movie> getAllMovies() {
        try (EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager()) {
            return movieDao.findAll(em);
        }
    }
}