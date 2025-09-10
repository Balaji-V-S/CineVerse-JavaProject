package com.cineverse.dao;

import com.cineverse.entity.Movie;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class MovieDaoImpl implements MovieDao {
    @Override
    public void save(Movie movie, EntityManager em) { em.persist(movie); }

    @Override
    public void update(Movie movie, EntityManager em) { em.merge(movie); }

    @Override
    public Optional<Movie> findById(long id, EntityManager em) { return Optional.ofNullable(em.find(Movie.class, id)); }

    @Override
    public List<Movie> findAll(EntityManager em) { return em.createQuery("SELECT m FROM Movie m", Movie.class).getResultList(); }
}