package com.robincopy.robincopyapi;

import com.robincopy.robincopyapi.dto.PortfolioSummaryDto;
import com.robincopy.robincopyapi.dto.StockInfoDto;
import com.robincopy.robincopyapi.dto.StockReducedInfoDto;
import com.robincopy.robincopyapi.dto.SummaryStockInfoDto;
import com.robincopy.robincopyapi.mock.FakeStockInfoRepository;
import com.robincopy.robincopyapi.models.PriceStatus;
import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import com.robincopy.robincopyapi.util.PortfolioIndicatorsCalculator;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
class PortfolioCalculatorTests {

    static final long START_TIME = 1592257937L;
    static final long END_TIME = 1623793828L;

    private final FakeStockInfoRepository fakeStockInfoRepository = new FakeStockInfoRepository();

    @Test
    void test01_TestIndicatorCalculator_ReturningCorrectDailyProfit() {
        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);

        StockInfoDto indicators = PortfolioIndicatorsCalculator.getStockInfo(share, fakeStockInfoRepository, START_TIME, END_TIME,"D");
        assertThat(indicators.getDayProfit()).isEqualTo(75.0);
    }

    @Test
    void test02_TestIndicatorCalculator_ReturningCorrectPrice() {
        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);

        StockInfoDto indicators = PortfolioIndicatorsCalculator.getStockInfo(share, fakeStockInfoRepository, START_TIME, END_TIME,"D");
        assertThat(indicators.getPrice()).isEqualTo(510.0);
    }

    @Test
    void test03_TestIndicatorCalculator_ReturningCorrectYearHigh() {
        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);

        StockInfoDto indicators = PortfolioIndicatorsCalculator.getStockInfo(share, fakeStockInfoRepository, START_TIME, END_TIME,"D");
        assertThat(indicators.getYearHigh()).isEqualTo(580.0);
    }

    @Test
    void test04_TestIndicatorCalculator_ReturningCorrectYearLow() {
        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);

        StockInfoDto indicators = PortfolioIndicatorsCalculator.getStockInfo(share, fakeStockInfoRepository, START_TIME, END_TIME,"D");
        assertThat(indicators.getYearLow()).isEqualTo(460.0);
    }

    @Test
    void test05_TestIndicatorCalculator_ReturningCorrectAverageVolume() {
        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);

        StockInfoDto indicators = PortfolioIndicatorsCalculator.getStockInfo(share, fakeStockInfoRepository, START_TIME, END_TIME,"D");
        assertThat(indicators.getAvgVolume()).isEqualTo(33.333333333333336);
    }

    @Test
    void test06_TestIndicatorCalculator_ReturningCorrectPriceToEarningRatio() {
        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);

        StockInfoDto indicators = PortfolioIndicatorsCalculator.getStockInfo(share, fakeStockInfoRepository, START_TIME, END_TIME,"D");
        assertThat(indicators.getPeRatio()).isEqualTo(10.2);
    }

    @Test
    void test07_TestIndicatorCalculator_ReturningCorrectDividendYield() {
        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);

        StockInfoDto indicators = PortfolioIndicatorsCalculator.getStockInfo(share, fakeStockInfoRepository, START_TIME, END_TIME,"D");
        assertThat(indicators.getDivYield()).isEqualTo(498.921568627451);
    }

    @Test
    void test08_TestIndicatorCalculator_ReturningCorrectProfit() {
        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);

        StockInfoDto indicators = PortfolioIndicatorsCalculator.getStockInfo(share, fakeStockInfoRepository, START_TIME, END_TIME,"D");
        assertThat(indicators.getProfit()).isEqualTo(50.0);
    }

    @Test
    void test09_TestIndicatorCalculator_ReturningCorrectProfitPercentage() {
        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);

        StockInfoDto indicators = PortfolioIndicatorsCalculator.getStockInfo(share, fakeStockInfoRepository, START_TIME, END_TIME,"D");
        assertThat(indicators.getProfitPercentage()).isEqualTo(2.0);
    }

    @Test
    void test10_TestIndicatorCalculator_ReturningCorrectDayVariationPercentage() {
        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);

        StockInfoDto indicators = PortfolioIndicatorsCalculator.getStockInfo(share, fakeStockInfoRepository, START_TIME, END_TIME,"D");
        assertThat(indicators.getDayVariationPercentage()).isEqualTo(3.1372549019607843);
    }


    @Test
    void test11_TestIndicatorCalculator_ReturningCorrectPriceStatus() {
        User user = new User("firstname", "lastname");
        Share share = new Share(5, user, "TSLA", 500.0);

        StockReducedInfoDto stockReducedInfoDto = PortfolioIndicatorsCalculator.getStockReducedInfo(share, 3, fakeStockInfoRepository, START_TIME, END_TIME, "D");
        assertThat(stockReducedInfoDto.getPriceStatus()).isEqualTo(PriceStatus.INCREASED);
    }

    @Test
    void test12_TestIndicatorCalculator_ReturningCorrectBalance() {
        User user = new User("firstname", "lastname");
        user.buyShare(new Share(5, user, "TSLA", 500.0));

        PortfolioSummaryDto portfolioSummaryDto = PortfolioIndicatorsCalculator.getPortfolioSummaryDto(user, new FakeStockInfoRepository());

        assertThat(portfolioSummaryDto.getBalance()).isEqualTo(2550.0);
    }

    @Test
    void test13_TestIndicatorCalculator_ReturningCorrectBalanceIncreasePercentage() {
        User user = new User("firstname", "lastname");
        user.buyShare(new Share(5, user, "TSLA", 500.0));

        PortfolioSummaryDto portfolioSummaryDto = PortfolioIndicatorsCalculator.getPortfolioSummaryDto(user, new FakeStockInfoRepository());

        assertThat(portfolioSummaryDto.getIncreasePercentage()).isEqualTo(2.0);
    }

    @Test
    void test14_TestIndicatorCalculator_ReturningCorrectOwnedStockSummary() {
        User user = new User("firstname", "lastname");
        user.buyShare(new Share(5, user, "TSLA", 500.0));

        PortfolioSummaryDto portfolioSummaryDto = PortfolioIndicatorsCalculator.getPortfolioSummaryDto(user, new FakeStockInfoRepository());

        SummaryStockInfoDto expectedSummaryStockInfoDto = new SummaryStockInfoDto("TSLA", 5, 510.0, 3.1372549019607843, 16.0, 1.9607843137254901, 49.01960784313726);
        assertThat(portfolioSummaryDto.getStocksInfo().get(0)).isEqualTo(expectedSummaryStockInfoDto);
    }
}
