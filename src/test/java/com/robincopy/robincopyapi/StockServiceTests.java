package com.robincopy.robincopyapi;

import com.robincopy.robincopyapi.dto.*;
import com.robincopy.robincopyapi.mock.StockMocks;
import com.robincopy.robincopyapi.models.PriceStatus;
import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import com.robincopy.robincopyapi.repositories.StockInfoRepository;
import com.robincopy.robincopyapi.services.StockService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class StockServiceTests {

    @Autowired
    StockService stockService;

    @MockBean
    StockInfoRepository stockInfoRepository;

    /*
        * Test Stock Service stock info generation, info gets feed from StockInfoRepository.
        * Test Stock Service reduced info generation.
        * Test Stock Service Stocks summary report.
     */

    @Test
    void generateStockInfo() {
        when(stockInfoRepository.getStockDetails(isA(String.class))).thenReturn(StockMocks.getStockDetails());
        when(stockInfoRepository.getStockInfo(isA(String.class), isA(Long.class), isA(Long.class), isA(String.class))).thenReturn(StockMocks.getStockInfoDto());
        when(stockInfoRepository.getStockQuote(isA(String.class))).thenReturn(StockMocks.getStockQuoteInfo());

        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);

        StockInfo stockInfo = stockService.generateStockInfo(share, "D");
        assertThat(stockInfo.getCompanyName()).isEqualTo("Tesla");
        assertThat(stockInfo.getStockSymbol()).isEqualTo("TSLA");
        assertThat(stockInfo.getDayProfit()).isEqualTo(75.0);
        assertThat(stockInfo.getPrice()).isEqualTo(510.0);
        assertThat(stockInfo.getStockPrices().get(0).getPrice()).isEqualTo(550.0);
        assertThat(stockInfo.getStockPrices().get(0).getDate()).isEqualTo(1616533398L);
        assertThat(stockInfo.getOpenValue()).isEqualTo(495.0);
        assertThat(stockInfo.getDayHigh()).isEqualTo(505.0);
        assertThat(stockInfo.getDayLow()).isEqualTo(490.0);
        assertThat(stockInfo.getYearHigh()).isEqualTo(580.0);
        assertThat(stockInfo.getYearLow()).isEqualTo(460.0);
        assertThat(stockInfo.getAvgVolume()).isEqualTo(33.333333333333336);
        assertThat(stockInfo.getPeRatio()).isEqualTo(10.2);
        assertThat(stockInfo.getDivYield()).isEqualTo(498.921568627451);
        assertThat(stockInfo.getMarketCap()).isEqualTo(500000);
        assertThat(stockInfo.getCompanyDescription()).isEqualTo("Tesla is a Automobile company. It's official site is https://tesla.com");
        assertThat(stockInfo.getProfit()).isEqualTo(50.0);
        assertThat(stockInfo.getProfitPercentage()).isEqualTo(2.0);
        assertThat(stockInfo.getDayVariationPercentage()).isEqualTo(3.1372549019607843);
    }

    @Test
    void generateReducedStockInfo() {
        when(stockInfoRepository.getStockDetails(isA(String.class))).thenReturn(StockMocks.getStockDetails());
        when(stockInfoRepository.getStockInfo(isA(String.class), isA(Long.class), isA(Long.class), isA(String.class))).thenReturn(StockMocks.getStockInfoDto());
        when(stockInfoRepository.getStockQuote(isA(String.class))).thenReturn(StockMocks.getStockQuoteInfo());

        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);

        StockReducedInfo stockReducedInfo = stockService.generateReducedStockInfo(share, "D", 3);
        assertThat(stockReducedInfo.getStockSymbol()).isEqualTo("TSLA");
        assertThat(stockReducedInfo.getPrice()).isEqualTo(510.0);
        assertThat(stockReducedInfo.getSharesQuantity()).isEqualTo(5);
        assertThat(stockReducedInfo.getPriceStatus()).isEqualTo(PriceStatus.INCREASED);
        assertThat(stockReducedInfo.getStockPrices().get(0).getPrice()).isEqualTo(550.0);
        assertThat(stockReducedInfo.getStockPrices().get(0).getDate()).isEqualTo(1616533398L);
    }

    @Test
    void generateStockSummary() {
        when(stockInfoRepository.getStockQuote(isA(String.class))).thenReturn(StockMocks.getStockQuoteInfo());
        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);
        user.setInvestedBalance(2500.0);
        user.setShares(Collections.singletonList(share));
        StockSummaryInfo expectedStockSummaryInfo = new StockSummaryInfo("TSLA", 5, 510.0, 3.1372549019607843, 16.0, 1.9607843137254901, 49.01960784313726);

        StockSummary stockSummary = stockService.getUserStockSummary(user);
        assertThat(stockSummary.getBalance()).isEqualTo(2550.0);
        assertThat(stockSummary.getIncreasePercentage()).isEqualTo(2.0);
        assertThat(stockSummary.getStocksInfo().get(0)).isEqualTo(expectedStockSummaryInfo);
    }

}
