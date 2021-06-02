package com.robincopy.robincopyapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.robincopy.robincopyapi.controllers.UserController;
import com.robincopy.robincopyapi.dto.*;
import com.robincopy.robincopyapi.mock.StockMocks;
import com.robincopy.robincopyapi.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    UserService userService;


    /* TEST INTERNAL API, testing controller is effectively receiving requests and delegating processing to service
         * Creating a user should return created user info
         * Adding shares, should update owned shares
         * Getting user shares list, should return owned shares and the following information
                    * Stock Symbol
                    * Price
                    * Price History for the last 10 days (in order to make a graph)
                    * Shares on hold
                    * Price Status (it increased, decreased or remained equal during the current day)
        * Getting user stock details, should return the following information
                    * Company name
                    * Daily profit ($)
                    * Past year stock price history
                    * Actual price
                    * Open value
                    * Day low
                    * Day high
                    * Year high
                    * Year low
                    * Average Sales Volume
                    * Price/Earning ratio
                    * Dividend Yield
                    * Market capitalization
                    * Company Description
                    * Total Profit
                    * Profit Percentage
                    * Daily variation (%)
        * Getting user stocks summary, should return user portfolio summary, including the following information
                    * List of the owned shares, including symbol, quantity owned, last stock price, daily variation ($, %), total variation and total winnings for the given stock
                    * Total balance (sum of owned shares prices)
                    * Winning percentage
     */

    @Test
    void creatingNewUser_ShouldReturnNewUserInfo() throws Exception {
        UserDto user = new UserDto("name", "lastName");

        when(userService.addUser(isA(UserDto.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)))
                .andReturn();
    }

    @Test
    void addingShares_ShouldUpdateSharesQuantity() throws Exception {
        ShareAddedDto share = new ShareAddedDto("userId", "TSLA", 5, 500.0);

        when(userService.addShare(isA(NewShareDto.class))).thenReturn(share);

        mockMvc.perform(post("/users/shares")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(share)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(share)))
                .andReturn();
    }

    @Test
    void getUserStocksList_ShouldReturnListOfOwnedStocks() throws Exception {
        when(userService.getUserStockInfo(isA(String.class))).thenReturn(StockMocks.getStockReducedInfos());

        mockMvc.perform(get("/users/1hasjg41sj4/shares")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(StockMocks.getStockReducedInfos())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(StockMocks.getStockReducedInfos())))
                .andReturn();
    }

    @Test
    void getUserStockInfo_ShouldReturnDetailedInformationAboutStock() throws Exception{
        when(userService.getUserStockInfo(isA(String.class), isA(String.class))).thenReturn(StockMocks.getStockInfo());

        mockMvc.perform(get("/users/1hasjg41sj4/shares/TSLA")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(StockMocks.getStockInfo())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(StockMocks.getStockInfo())))
                .andReturn();
    }

    @Test
    void getUserSummary_ShouldReturnUserPortfolioSummary() throws Exception {
        when(userService.getUserStockSummary(isA(String.class))).thenReturn(StockMocks.getStockSummatyInfo());

        mockMvc.perform(get("/users/1hasjg41sj4/summary")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(StockMocks.getStockSummatyInfo())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(StockMocks.getStockSummatyInfo())))
                .andReturn();
    }

}
