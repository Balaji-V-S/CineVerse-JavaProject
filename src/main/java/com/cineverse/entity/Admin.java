package com.cineverse.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    // This class is intentionally simple.
    // It inherits all fields from the User class.
    // The @DiscriminatorValue("ADMIN") annotation ensures that
    // when an Admin object is saved, the 'role' column in the
    // 'users' table will be set to "ADMIN".
}