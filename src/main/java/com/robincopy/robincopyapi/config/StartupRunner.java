package com.robincopy.robincopyapi.config;

import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import com.robincopy.robincopyapi.repositories.UserRepository;
import com.robincopy.robincopyapi.services.impl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Slf4j
@Component
public class StartupRunner {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public StartupRunner(UserRepository userRepository){

        this.userRepository = userRepository;
    }

    @PostConstruct
    private void init() {
        Optional<User> user = userRepository.findByFirstNameAndLastName("Juan", "Perez");
        if (user.isEmpty()) {
            User createdUser = new User("Juan", "Perez");
            createdUser.buyShare(new Share(4, createdUser, "TSLA", 580.0));
            createdUser.buyShare(new Share(5, createdUser, "FB", 340.0));
            createdUser.buyShare(new Share(2, createdUser, "AAPL", 130.0));
            createdUser = userRepository.save(createdUser);
            logger.info("User with id: " + createdUser.getId());
        } else {
            logger.info("User with id: " + user.get().getId());
        }
    }
}
