package com.robincopy.robincopyapi.mock;

import com.robincopy.robincopyapi.dto.*;
import com.robincopy.robincopyapi.models.PriceStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StockMocks {

    public static StockInfoDto getStockInfoDto(){
        return new StockInfoDto(Arrays.asList(450.0, 500.0, 550.0),
                Arrays.asList(460.0, 510.0, 580.0),
                Arrays.asList(440.0, 500.0, 540.0),
                Arrays.asList(445.0, 500.0, 530.0),
                Arrays.asList(1614718998L, 1615842198L, 1616533398L),
                "OK",
                Arrays.asList(30.0, 20.0, 50.0));
    }

    public static List<StockReducedInfo> getStockReducedInfos(){
        List<StockReducedInfo> stocks = new ArrayList<>();
        List<StockPrice> stockPrices1 = new ArrayList<>();
        stockPrices1.add(new StockPrice(350.0, 1614718998L));
        stockPrices1.add(new StockPrice(355.0, 1615842198L));
        stockPrices1.add(new StockPrice(360.0, 1616533398L));
        stocks.add(new StockReducedInfo("TSLA", 500.0, stockPrices1, 3, PriceStatus.INCREASED));
        List<StockPrice> stockPrices2 = new ArrayList<>();
        stockPrices2.add(new StockPrice(120.0, 1614718998L));
        stockPrices2.add(new StockPrice(100.0, 1615842198L));
        stockPrices2.add(new StockPrice(80.0, 1616533398L));
        stocks.add(new StockReducedInfo("AAPL", 500.0, stockPrices2, 3, PriceStatus.DECREASED));
        return stocks;
    }

    public static StockQuoteInfo getStockQuoteInfo(){
        return new StockQuoteInfo(510.0, 505.0, 490.0, 495.0, 494.0, 1616533398L);
    }


    public static StockDetails getStockDetails() {
        return new StockDetails(500000L, "https://tesla.com", "Automobile", "Tesla", "TSLA");
    }

    public static List<StockPrice> getStockPrices() {
        List<StockPrice> stockPrices = new ArrayList<>();
        stockPrices.add(new StockPrice(350.0, 1614718998L));
        stockPrices.add(new StockPrice(355.0, 1615842198L));
        stockPrices.add(new StockPrice(360.0, 1616533398L));
        return stockPrices;
    }

    public static StockInfo getStockInfo() {
        return StockInfo.builder()
                .companyName("Tesla")
                .stockSymbol("TSLA")
                .dayProfit(6.0) //define
                .price(500.0)
                .stockPrices(getStockPrices())
                .openValue(480.0)
                .dayLow(470.0)
                .dayHigh(520.0)
                .yearHigh(1000.0)
                .yearLow(200.0)
                .avgVolume(500000.0)
                .peRatio(4.5)
                .divYield(200.0)
                .marketCap(50000L)
                .companyDescription("Testa is an Automobiles company")
                .profit(50.0)
                .profitPercentage(10.0)
                .dayVariationPercentage(5.0)
                .build();

    }

    public static StockSummary getStockSummatyInfo() {
        List<StockSummaryInfo> summaryInfos = new ArrayList<>();
        summaryInfos.add(new StockSummaryInfo("TSLA", 5, 500.0, 5.0, 10.0, 100.0, 50.0));
        summaryInfos.add(new StockSummaryInfo("FB", 2, 50.0, 3.0, 4.0, 50.0, 20.0));
        summaryInfos.add(new StockSummaryInfo("AAPL", 4, 170.0, -1.0, -3.0, 80.0, 30.0));
        return new StockSummary(5000.0, 15.0, summaryInfos);
    }

    public static StockSummary getStockSummary() {
        List<StockSummaryInfo> stockSummaryInfos = getStockSummaryInfos();
        return new StockSummary(2000.0, 5.0, stockSummaryInfos);
    }

    public static List<StockSummaryInfo> getStockSummaryInfos() {
        List<StockSummaryInfo> infos = new ArrayList<>();
        infos.add(new StockSummaryInfo("TSLA", 4, 500.0, 4.0, 10.0, 20.0, 40.0));
        return infos;
    }
}
