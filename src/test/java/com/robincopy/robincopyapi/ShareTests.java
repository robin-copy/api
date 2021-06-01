package com.robincopy.robincopyapi;

import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class ShareTests {

    /* Share
     * When adding a share, average buy price should change
     * When removing a share from a user, the share average buy price should not change
     * When removing a share and the final quantity is zero, the share average buy price should change to zero
     */

    @Test
    void addingShare_ShouldChangeAverageBuyPrice() {
        User user = new User("test", "test_last");
        Share share = new Share(1, user, "TSLA", 100.0);
        share.increaseQuantity(1, 200.0);
        assertThat(share.getQuantity()).isEqualTo(2);
        assertThat(share.getAverageBuyPrice()).isEqualTo(150.0);
    }

    @Test
    void removingShare_ShouldNotChangeAverageBuyPrice() {
        User user = new User("test", "test_last");
        Share share = new Share(2, user, "TSLA", 100.0);
        share.decreaseQuantity(1);
        assertThat(share.getQuantity()).isEqualTo(1);
        assertThat(share.getAverageBuyPrice()).isEqualTo(100.0);
    }

    @Test
    void removingShareWithFinalQuantityZero_ShouldChangeAveragePriceToZero() {
        User user = new User("test", "test_last");
        Share share = new Share(1, user, "TSLA", 100.0);
        share.decreaseQuantity(1);
        assertThat(share.getQuantity()).isEqualTo(0);
        assertThat(share.getAverageBuyPrice()).isEqualTo(0.0);
    }
}
