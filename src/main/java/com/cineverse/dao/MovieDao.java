package com.cineverse.dao;

import com.cineverse.entity.Movie;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public interface MovieDao {
    void save(Movie movie, EntityManager em);
    void update(Movie movie, EntityManager em);
    Optional<Movie> findById(long id, EntityManager em);
    List<Movie> findAll(EntityManager em);
}