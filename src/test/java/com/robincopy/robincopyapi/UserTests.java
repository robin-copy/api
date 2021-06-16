package com.robincopy.robincopyapi;

import com.robincopy.robincopyapi.exceptions.NotFoundException;
import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
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

    /* USERS
     * New user should have no shares
     * When buying a share to a user, user shares should include it
     * When buying a repeated share to a user, user share should increase quantity
     * When buying different shares to a user, user should include all of them
     * When selling a share from a user, user shares should not include it
     * When selling a share from a user(with quantity > 1), user share should decrease quantity
     * selling non existing share, should throw error
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
    void test01_buyingShare_ShouldAddToSharesList() {
        User user = new User("test", "name");
        Share share = new Share(1, user, "TSLA", 100.0);
        user.buyShare(share);
        assertThat(user.getShares().size()).isEqualTo(1);
        assertThat(getUserShare(user, "TSLA")).isPresent();
    }

    @Test
    void test02_buyingRepeatedShare_ShouldIncreaseQuantity() {
        User user = new User("test", "name");
        user.buyShare(new Share(1, user, "TSLA", 100.0));
        user.buyShare(new Share(3, user, "TSLA", 100.0));
        Optional<Share> share = getUserShare(user, "TSLA");

        assertThat(share).isPresent();
        assertThat(share.get().getQuantity()).isEqualTo(4);
    }

    @Test
    void test03_buyingDifferentShares_ShouldIncludeAllOfThem() {
        User user = new User("test", "name");
        user.buyShare(new Share(1, user, "TSLA", 100.0));
        user.buyShare(new Share(1, user, "AAPL", 120.0));

        assertThat(user.getShares().size()).isEqualTo(2);

        assertThat(getUserShare(user, "TSLA")).isPresent();
        assertThat(getUserShare(user, "AAPL")).isPresent();
    }

    @Test
    void test04_sellingShare_ShouldRemoveFromSharesList() {
        User user = new User("test", "name");
        user.buyShare(new Share(1, user, "TSLA", 100.0));
        assertThat(user.getShares().size()).isEqualTo(1);
        assertThat(getUserShare(user, "TSLA")).isPresent();

        user.sellShare(new Share(1, user, "TSLA", 100.0));
        assertThat(user.getShares()).isEmpty();
    }

    @Test
    void test05_sellingShare_ShouldDecreaseShareQuantity() {
        User user = new User("test", "name");
        user.buyShare(new Share(3, user, "TSLA", 100.0));

        assertThat(user.getShares().size()).isEqualTo(1);
        Optional<Share> share = getUserShare(user, "TSLA");
        assertThat(share).isPresent();
        assertThat(share.get().getQuantity()).isEqualTo(3);

        user.sellShare(new Share(1, user, "TSLA", 100.0));

        assertThat(user.getShares().size()).isEqualTo(1);
        share = getUserShare(user, "TSLA");
        assertThat(share).isPresent();
        assertThat(share.get().getQuantity()).isEqualTo(2);
    }

    @Test()
    void test06_sellingInvalidShare_ShouldThrowException() {
        User user = new User("name", "lastname");
        Share share = new Share(1, user, "TSLA", 100.0);

        Throwable exception = assertThrows(NotFoundException.class, () -> user.sellShare(share));
        assertThat(exception.getMessage()).isEqualTo("Share not found");
    }


    private Optional<Share> getUserShare(User user, String symbol) {
        return user.getShares().stream().filter(share -> share.getStockSymbol().equals(symbol)).findFirst();
    }

}
