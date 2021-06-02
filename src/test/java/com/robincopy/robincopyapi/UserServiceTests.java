package com.robincopy.robincopyapi;

import com.robincopy.robincopyapi.dto.*;
import com.robincopy.robincopyapi.exceptions.BadRequestException;
import com.robincopy.robincopyapi.mock.StockMocks;
import com.robincopy.robincopyapi.models.PriceStatus;
import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import com.robincopy.robincopyapi.repositories.StockInfoRepository;
import com.robincopy.robincopyapi.repositories.UserRepository;
import com.robincopy.robincopyapi.services.StockService;
import com.robincopy.robincopyapi.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class UserServiceTests {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @MockBean
    StockService stockService;

    @MockBean
    StockInfoRepository stockInfoRepository;

    /* User Service tests
        * When adding a new user, service should check if name and lastname combination is in use and save to db.
        * When adding a share to a user, user service should find user by userId, ask for the current price of this stock and add this to his shares.
        * Should return the list of stocks owned by the user and the necessary stock information provided by the StockService
        * Should return detailed information (provided by the StockService) about a certain share of the user
        * Should return user portfolio summary
     */

    @Test
    void addingAUser_ShouldStoreInDB() {
        UserDto userDto = new UserDto("name1", "lastname1");
        userService.addUser(userDto);
        assertThat(userRepository.existsByFirstNameAndLastName("name1", "lastname1")).isTrue();
    }

    @Test
    void addingAUserWithRepeatedNameAndLastName_ShouldNotStoreInDB() {
        UserDto userDto = new UserDto("name2", "lastname2");
        userService.addUser(userDto);
        assertThat(userRepository.existsByFirstNameAndLastName("name2", "lastname2"));

        Throwable exception = assertThrows(BadRequestException.class, () -> userService.addUser(userDto));
        assertThat(exception.getMessage()).isEqualTo("User already exists");
    }

    @Test
    void addingShareToUser_ShouldAddToUserShareList() {
        when(stockInfoRepository.getStockQuote(isA(String.class))).thenReturn(StockMocks.getStockQuoteInfo());

        UserDto userDto = new UserDto("name3", "lastname3");
        userDto = userService.addUser(userDto);

        NewShareDto shareDto = new NewShareDto(userDto.getUserId(), "TSLA", 4);
        ShareAddedDto share = userService.addShare(shareDto);
        assertThat(share.getPrice()).isEqualTo(510.0);
        assertThat(share.getQuantity()).isEqualTo(4);
        assertThat(share.getSymbol()).isEqualTo("TSLA");
    }

    @Test
    void gettingUserStockInfo_ShouldReturnUserOwnedSharesInfo() {
        when(stockService.generateReducedStockInfo(isA(Share.class), isA(String.class), isA(Integer.class))).thenReturn(StockMocks.getStockReducedInfos().get(0));
        when(stockInfoRepository.getStockQuote(isA(String.class))).thenReturn(StockMocks.getStockQuoteInfo());
        when(stockInfoRepository.getStockInfo(isA(String.class), isA(Long.class), isA(Long.class), isA(String.class))).thenReturn(StockMocks.getStockInfoDto());

        UserDto userDto = new UserDto("name4", "lastname4");
        userDto = userService.addUser(userDto);
        NewShareDto shareDto = new NewShareDto(userDto.getUserId(), "TSLA", 4);
        userService.addShare(shareDto);
        List<StockReducedInfo> stocks = userService.getUserStockInfo(userDto.getUserId());
        assertThat(stocks.size()).isEqualTo(1);
        assertThat(stocks.get(0).getPrice()).isEqualTo(500.0);
        assertThat(stocks.get(0).getPriceStatus()).isEqualTo(PriceStatus.INCREASED);
        assertThat(stocks.get(0).getStockSymbol()).isEqualTo("TSLA");
    }

    @Test
    void gettingUserStockInfo_ShouldReturnCertainStockInfo() {
        when(stockInfoRepository.getStockQuote(isA(String.class))).thenReturn(StockMocks.getStockQuoteInfo());
        when(stockInfoRepository.getStockInfo(isA(String.class), isA(Long.class), isA(Long.class), isA(String.class))).thenReturn(StockMocks.getStockInfoDto());
        when(stockInfoRepository.getStockDetails(isA(String.class))).thenReturn(StockMocks.getStockDetails());

        when(stockService.generateStockInfo(isA(Share.class), isA(String.class))).thenReturn(StockMocks.getStockInfo());

        UserDto userDto = new UserDto("name5", "lastname5");
        userDto = userService.addUser(userDto);
        NewShareDto shareDto = new NewShareDto(userDto.getUserId(), "TSLA", 4);
        userService.addShare(shareDto);

        StockInfo stockInfo = userService.getUserStockInfo(userDto.getUserId(), "TSLA");
        assertThat(stockInfo.getCompanyName()).isEqualTo("Tesla");
        assertThat(stockInfo.getStockSymbol()).isEqualTo("TSLA");
        assertThat(stockInfo.getDayProfit()).isEqualTo(6.0);
        assertThat(stockInfo.getPrice()).isEqualTo(500.0);
    }

    @Test
    void getUserStockSummary_ShouldReturnUserPortfolioSummary() {
        when(stockInfoRepository.getStockQuote(isA(String.class))).thenReturn(StockMocks.getStockQuoteInfo());
        when(stockService.getUserStockSummary(isA(User.class))).thenReturn(StockMocks.getStockSummary());

        UserDto userDto = new UserDto("name6", "lastname6");
        userDto = userService.addUser(userDto);
        NewShareDto shareDto = new NewShareDto(userDto.getUserId(), "TSLA", 4);
        userService.addShare(shareDto);

        StockSummary summary = userService.getUserStockSummary(userDto.getUserId());

        assertThat(summary.getBalance()).isEqualTo(2000.0);
        assertThat(summary.getIncreasePercentage()).isEqualTo(5.0);
        assertThat(summary.getStocksInfo().size()).isEqualTo(1);
        assertThat(summary.getStocksInfo().get(0).getStockSymbol()).isEqualTo("TSLA");
    }
}
