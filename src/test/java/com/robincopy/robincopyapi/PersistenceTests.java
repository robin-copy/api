package com.robincopy.robincopyapi;

import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import com.robincopy.robincopyapi.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class PersistenceTests {

    @Autowired
    private UserRepository userRepository;

    // PERSISTENCE TESTS

    @Test
    void test01_createdUser_ShouldBePersisted() {
        User user = new User("name", "lastname");
        user.buyShare(new Share(3, user, "TSLA", 100.0));
        user = userRepository.save(user);

        Optional<User> persistedUser = userRepository.findById(user.getId());
        assertThat(persistedUser).isPresent();
    }

    @Test
    void test02_deletedUser_ShouldNotBePersisted() {
        User user = new User("name", "lastname");
        user = userRepository.save(user);

        userRepository.deleteById(user.getId());

        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void test03_modifiedUser_ShouldBePersisted() {
        User user = new User("name", "lastname");
        user.buyShare(new Share(3, user, "TSLA", 100.0));
        user = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isPresent();
        foundUser.get().buyShare(new Share(2, user, "TSLA", 120.0));
        userRepository.save(foundUser.get());

        Optional<User> persistedUser = userRepository.findById(user.getId());
        assertThat(persistedUser).isPresent();
        assertThat(persistedUser.get().getShares().get(0).getQuantity()).isEqualTo(5);
    }
}
