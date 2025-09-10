package com.cineverse.dao;

import com.cineverse.entity.ShowTime;
import jakarta.persistence.EntityManager;
import java.util.Optional;

public class ShowTimeDaoImpl implements ShowTimeDao {
    @Override
    public Optional<ShowTime> findById(long id, EntityManager em) { return Optional.ofNullable(em.find(ShowTime.class, id)); }
}