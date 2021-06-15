package com.robincopy.robincopyapi.repositories;

import com.robincopy.robincopyapi.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
    Optional<User> findByFirstNameAndLastName(String firstName, String lastName);
}
