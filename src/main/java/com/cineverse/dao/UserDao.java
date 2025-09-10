package com.cineverse.dao;

import com.cineverse.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.Optional;

public class UserDao {
    public void save(User user, EntityManager em) { em.persist(user); }
    public Optional<User> findById(long id, EntityManager em) { return Optional.ofNullable(em.find(User.class, id)); }
    public Optional<User> findByEmail(String email, EntityManager em) {
        try {
            return Optional.of(em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                    .setParameter("email", email).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}