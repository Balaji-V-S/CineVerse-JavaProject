// File: src/main/java/com/cineverse/service/UserService.java
package com.cineverse.service;

import com.cineverse.dao.UserDao;
import com.cineverse.dao.UserDaoImpl;
import com.cineverse.entity.*;
import com.cineverse.exception.UserAlreadyExistsException;
import com.cineverse.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

public class UserService {
    private final UserDao userDao = new UserDaoImpl();
    public User registerUser(String name, String email, String password, boolean isAdmin) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            userDao.findByEmail(email, em).ifPresent(u -> {
                throw new UserAlreadyExistsException("User with email " + email + " already exists.");
            });
            User user = isAdmin ? new Admin() : new Customer();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            userDao.save(user, em);
            tx.commit();
            return user;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
