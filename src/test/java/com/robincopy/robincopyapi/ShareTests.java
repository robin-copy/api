package com.robincopy.robincopyapi;

import com.robincopy.robincopyapi.exceptions.BadRequestException;
import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class ShareTests {

    /* Share
     * When adding a share, average buy price should change
     * When adding a share and the amount parameter is negative or zero, should throw an error
     * When removing a share from a user, the share average buy price should not change
     * When removing a share ant the final quantity is negative, should throw an error
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
    void addingShareAndAmountParameterIsNegativeOrZero_ShouldThrowAnError() {
        User user = new User("test", "test_last");
        Share share = new Share(1, user, "TSLA", 100.0);
        Throwable exceptionZero = assertThrows(BadRequestException.class, () -> share.increaseQuantity(0, 200.0));
        Throwable exceptionNegative = assertThrows(BadRequestException.class, () -> share.increaseQuantity(-1, 200.0));
        assertThat(exceptionZero.getMessage()).isEqualTo("Share amount parameter can't be less or equal to zero");
        assertThat(exceptionNegative.getMessage()).isEqualTo("Share amount parameter can't be less or equal to zero");
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
    void removingShareWithFinalQuantityNegative_ShouldThrowAnError() {
        User user = new User("test", "test_last");
        Share share = new Share(1, user, "TSLA", 100.0);
        Throwable exception = assertThrows(BadRequestException.class, () -> share.decreaseQuantity(2));
        assertThat(exception.getMessage()).isEqualTo("Updated quantity can't be negative");
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
