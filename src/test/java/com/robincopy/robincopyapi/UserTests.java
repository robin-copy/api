package com.robincopy.robincopyapi;

import com.robincopy.robincopyapi.exceptions.NotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class UserTests {

    @Autowired
    private UserRepository userRepository;

    /* USERS
     * New user should have no shares
     * When adding a share to a user, user shares should include it
     * When adding a repeated share to a user, user share should increase quantity
     * When adding different shares to a user, user should include all of them
     * When removing a share from a user, user shares should not include it
     * When removing a share from a user(with quantity > 1), user share should decrease quantity
     * Removing non existing share, should throw error
     * ---
     * USERS DB PERSISTENCE
     * When saving a user , it can be read from db
     * When deleting a user, it can no longer be read from db
     * When modifying a user and saving it, it modifies on db
     */

    @Test
    void emptyUser_ShouldHaveNoShares() {
        User user = new User("test", "name");
        assertThat(user.getShares()).isEmpty();
    }

    @Test
    void addingShare_ShouldAddToSharesList() {
        User user = new User("test", "name");
        user.addShare(1, 100.0, "TSLA");
        assertThat(user.getShares().size()).isEqualTo(1);
        assertThat(user.getShares().stream().filter(share -> share.getStockSymbol().equals("TSLA")).findFirst()).isPresent();
    }

    @Test
    void addingRepeatedShare_ShouldIncreaseQuantity() {
        User user = new User("test", "name");
        user.addShare(1, 100.0, "TSLA");
        user.addShare( 3, 100.0, "TSLA");
        assertThat(user.getShares().stream().filter(share -> share.getStockSymbol().equals("TSLA")).findFirst().get().getQuantity()).isEqualTo(4);
    }

    @Test
    void addingDifferentShares_ShouldIncludeAllOfThem() {
        User user = new User("test", "name");
        user.addShare( 1, 100.0, "TSLA");
        user.addShare(1, 120.0, "APPL");
        assertThat(user.getShares().size()).isEqualTo(2);
        assertThat(user.getShares().stream().filter(share -> share.getStockSymbol().equals("TSLA")).findFirst()).isPresent();
        assertThat(user.getShares().stream().filter(share -> share.getStockSymbol().equals("APPL")).findFirst()).isPresent();
    }

    @Test
    void removingShare_ShouldRemoveFromSharesList() {
        User user = new User("test", "name");
        user.addShare(1, 100.0, "TSLA");
        assertThat(user.getShares().size()).isEqualTo(1);
        assertThat(user.getShares().stream().filter(share -> share.getStockSymbol().equals("TSLA")).findFirst()).isPresent();
        user.removeShare("TSLA", 1);
        assertThat(user.getShares()).isEmpty();
    }

    @Test
    void removingShare_ShouldDecreaseShareQuantity() {
        User user = new User("test", "name");
        user.addShare(3, 100.0, "TSLA");
        assertThat(user.getShares().size()).isEqualTo(1);
        assertThat(user.getShares().stream().filter(share -> share.getStockSymbol().equals("TSLA")).findFirst().get().getQuantity()).isEqualTo(3);
        user.removeShare("TSLA", 1);
        assertThat(user.getShares().size()).isEqualTo(1);
        assertThat(user.getShares().stream().filter(share -> share.getStockSymbol().equals("TSLA")).findFirst().get().getQuantity()).isEqualTo(2);
    }

    @Test()
    void removingInvalidShare_ShouldThrowException() {
        User user = new User("name", "lastname");
        Throwable exception = assertThrows(NotFoundException.class, () -> user.removeShare("TSLA", 1));
        assertThat(exception.getMessage()).isEqualTo("Share not found");
    }

    // PERSISTENCE TESTS

    @Test
    void createdUser_ShouldBePersisted() {
        User user = new User("name", "lastname");
        user.addShare( 3, 100.0, "TSLA");
        user = userRepository.save(user);

        Optional<User> persistedUser = userRepository.findById(user.getId());
        assertThat(persistedUser).isPresent();
        assertThat(persistedUser.get().getId()).isEqualTo(user.getId());
        assertThat(persistedUser.get().getFirstName()).isEqualTo("name");
        assertThat(persistedUser.get().getLastName()).isEqualTo("lastname");
        assertThat(persistedUser.get().getShares().get(0).getQuantity()).isEqualTo(3);
    }

    @Test
    void deletedUser_ShouldNotBePersisted() {
        User user = new User("name", "lastname");
        user = userRepository.save(user);

        userRepository.deleteById(user.getId());

        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertThat(deletedUser).isEmpty();
    }

    @Test
    void modifiedUser_ShouldBePersisted() {
        User user = new User("name", "lastname");
        user.addShare( 3, 100.0, "TSLA");
        user = userRepository.save(user);

        Optional<User> foundUser = userRepository.findById(user.getId());
        assertThat(foundUser).isPresent();
        foundUser.get().addShare(2, 120.0, "TSLA");
        userRepository.save(foundUser.get());

        Optional<User> persistedUser = userRepository.findById(user.getId());
        assertThat(persistedUser).isPresent();
        assertThat(persistedUser.get().getId()).isEqualTo(user.getId());
        assertThat(persistedUser.get().getFirstName()).isEqualTo("name");
        assertThat(persistedUser.get().getLastName()).isEqualTo("lastname");
        assertThat(persistedUser.get().getShares().get(0).getQuantity()).isEqualTo(5);
    }

}
