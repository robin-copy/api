package com.robincopy.robincopyapi.repositories;

import com.robincopy.robincopyapi.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    Boolean existsByFirstNameAndLastName(String firstName, String lastName);
}
