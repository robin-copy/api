package com.robincopy.robincopyapi;

import com.robincopy.robincopyapi.dto.*;
import com.robincopy.robincopyapi.mock.FakeStockInfoRepository;
import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import com.robincopy.robincopyapi.services.StockService;
import com.robincopy.robincopyapi.services.impl.StockServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class StockServiceTests {

    StockService stockService;

    /*
        * Test Stock Service stock info generation, info gets feed from StockInfoRepository.
        * Test Stock Service reduced info generation.
        * Test Stock Service Stocks summary report.
     */

    @BeforeEach
    public void startup() {
        this.stockService = new StockServiceImpl(new FakeStockInfoRepository());
    }

    @Test
    void test01_generateStockInfo() {
        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);

        StockInfoDto stockInfoDto = stockService.getStockInfo(share, "D");
        assertThat(stockInfoDto.getStockSymbol()).isEqualTo("TSLA");
    }

    @Test
    void test02_generateReducedStockInfo() {
        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);

        StockReducedInfoDto stockReducedInfoDto = stockService.getReducedStockInfo(share, "D", 3);
        assertThat(stockReducedInfoDto.getStockSymbol()).isEqualTo("TSLA");
        assertThat(stockReducedInfoDto.getSharesQuantity()).isEqualTo(5);
    }

    @Test
    void test03_generateStockSummary() {
        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);
        user.setInvestedBalance(2500.0);
        user.setShares(Collections.singletonList(share));

        PortfolioSummaryDto portfolioSummaryDto = stockService.getUserStockSummary(user);
        assertThat(portfolioSummaryDto.getStocksInfo().size()).isEqualTo(1);
    }

}
