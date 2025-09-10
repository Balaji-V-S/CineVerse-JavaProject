package com.cineverse.dao;

import com.cineverse.entity.User;
import jakarta.persistence.EntityManager;
import java.util.Optional;

public interface UserDao {
    void save(User user, EntityManager em);
    Optional<User> findById(long id, EntityManager em);
    Optional<User> findByEmail(String email, EntityManager em);
}