package com.cineverse.dao;

import com.cineverse.entity.ShowTime;
import jakarta.persistence.EntityManager;
import java.util.Optional;

public interface ShowTimeDao {
    Optional<ShowTime> findById(long id, EntityManager em);
}
