package com.robincopy.robincopyapi;

import com.robincopy.robincopyapi.dto.*;
import com.robincopy.robincopyapi.dto.share.BuyShareDto;
import com.robincopy.robincopyapi.dto.share.ShareDto;
import com.robincopy.robincopyapi.dto.user.UserDto;
import com.robincopy.robincopyapi.exceptions.BadRequestException;
import com.robincopy.robincopyapi.mock.FakeStockService;
import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.repositories.ShareRepository;
import com.robincopy.robincopyapi.repositories.UserRepository;
import com.robincopy.robincopyapi.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class UserServiceTests {

    UserServiceImpl userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ShareRepository shareRepository;

    /* User Service tests
        * When adding a new user, service should check if name and lastname combination is in use and save to db.
        * When adding a share to a user, user service should find user by userId, ask for the current price of this stock and add this to his shares.
        * Should return the list of stocks owned by the user and the necessary stock information provided by the StockService
        * Should return detailed information (provided by the StockService) about a certain share of the user
        * Should return user portfolio summary
     */

    @BeforeEach
    public void startup() {
        this.userService = new UserServiceImpl(userRepository, new FakeStockService());
    }

    @Test
    void test01_AddingAUser_ShouldStoreInDB() {
        UserDto userDto = new UserDto("name1", "lastname1");
        userService.addUser(userDto);
        assertThat(userRepository.existsByFirstNameAndLastName("name1", "lastname1")).isTrue();
    }

    @Test
    void test02_AddingAUserWithRepeatedNameAndLastName_ShouldNotStoreInDB() {
        UserDto userDto = new UserDto("name2", "lastname2");
        userService.addUser(userDto);

        Throwable exception = assertThrows(BadRequestException.class, () -> userService.addUser(userDto));
        assertThat(exception.getMessage()).isEqualTo("User already exists");
    }

    @Test
    void test03_BuyingAShare_ShouldAddToUserShareList() {
        UserDto userDto = new UserDto("name3", "lastname3");
        userDto = userService.addUser(userDto);

        BuyShareDto shareDto = new BuyShareDto(userDto.getUserId(), "TSLA", 4);
        ShareDto share = userService.buyShare(shareDto);
        assertThat(share.getQuantity()).isEqualTo(4);

        Optional<Share> foundShare = shareRepository.findByHolder_IdAndAndStockSymbol(userDto.getUserId(), share.getSymbol());
        assertThat(foundShare).isPresent();
        assertThat(foundShare.get().getQuantity()).isEqualTo(4);
    }

    @Test
    void test04_GettingUserStockInfo_ShouldReturnUserOwnedSharesInfo() {
        UserDto userDto = new UserDto("name4", "lastname4");
        userDto = userService.addUser(userDto);

        BuyShareDto shareDto = new BuyShareDto(userDto.getUserId(), "TSLA", 4);
        userService.buyShare(shareDto);

        List<StockReducedInfoDto> stocks = userService.getUserStockInfo(userDto.getUserId());
        assertThat(stocks.size()).isEqualTo(1);
    }

    @Test
    void test05_GettingUserStockInfo_ShouldReturnCertainStockInfo() {
        UserDto userDto = new UserDto("name5", "lastname5");
        userDto = userService.addUser(userDto);

        BuyShareDto shareDto = new BuyShareDto(userDto.getUserId(), "TSLA", 4);
        userService.buyShare(shareDto);

        StockInfoDto stockInfoDto = userService.getUserStockInfo(userDto.getUserId(), "TSLA");
        assertThat(stockInfoDto.getStockSymbol()).isEqualTo("TSLA");
    }

    @Test
    void test06_GetUserStockSummary_ShouldReturnUserPortfolioSummary() {
        UserDto userDto = new UserDto("name6", "lastname6");
        userDto = userService.addUser(userDto);

        BuyShareDto shareDto1 = new BuyShareDto(userDto.getUserId(), "TSLA", 4);
        BuyShareDto shareDto2 = new BuyShareDto(userDto.getUserId(), "FB", 2);
        BuyShareDto shareDto3 = new BuyShareDto(userDto.getUserId(), "AAPL", 4);
        userService.buyShare(shareDto1);
        userService.buyShare(shareDto2);
        userService.buyShare(shareDto3);

        PortfolioSummaryDto summary = userService.getUserStockSummary(userDto.getUserId());
        assertThat(summary.getStocksInfo().size()).isEqualTo(3);
    }
}
