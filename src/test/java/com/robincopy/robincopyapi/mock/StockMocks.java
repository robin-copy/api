package com.robincopy.robincopyapi.mock;

import com.robincopy.robincopyapi.dto.*;
import com.robincopy.robincopyapi.dto.api.StockInfo;
import com.robincopy.robincopyapi.dto.api.StockHistoricalDetails;
import com.robincopy.robincopyapi.dto.api.StockQuote;
import com.robincopy.robincopyapi.models.PriceStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StockMocks {

    public static StockHistoricalDetails getStockHistoricalDetails(){
        return new StockHistoricalDetails(Arrays.asList(450.0, 500.0, 550.0),
                Arrays.asList(460.0, 510.0, 580.0),
                Arrays.asList(440.0, 500.0, 540.0),
                Arrays.asList(445.0, 500.0, 530.0),
                Arrays.asList(1614718998L, 1615842198L, 1616533398L),
                "OK",
                Arrays.asList(30.0, 20.0, 50.0));
    }

    public static List<StockReducedInfoDto> getStockReducedInfos(){
        List<StockReducedInfoDto> stocks = new ArrayList<>();
        List<StockPriceDto> stockPrices1Dto = new ArrayList<>();
        stockPrices1Dto.add(new StockPriceDto(350.0, 1614718998L));
        stockPrices1Dto.add(new StockPriceDto(355.0, 1615842198L));
        stockPrices1Dto.add(new StockPriceDto(360.0, 1616533398L));
        stocks.add(new StockReducedInfoDto("TSLA", 500.0, stockPrices1Dto, 3, PriceStatus.INCREASED));
        List<StockPriceDto> stockPrices2Dto = new ArrayList<>();
        stockPrices2Dto.add(new StockPriceDto(120.0, 1614718998L));
        stockPrices2Dto.add(new StockPriceDto(100.0, 1615842198L));
        stockPrices2Dto.add(new StockPriceDto(80.0, 1616533398L));
        stocks.add(new StockReducedInfoDto("AAPL", 500.0, stockPrices2Dto, 3, PriceStatus.DECREASED));
        return stocks;
    }

    public static StockQuote getStockQuote(){
        return new StockQuote(510.0, 505.0, 490.0, 495.0, 494.0, 1616533398L);
    }


    public static StockInfo getStockInfo() {
        return new StockInfo(500000L, "https://tesla.com", "Automobile", "Tesla", "TSLA");
    }

    public static List<StockPriceDto> getStockPrices() {
        List<StockPriceDto> stockPriceDtos = new ArrayList<>();
        stockPriceDtos.add(new StockPriceDto(350.0, 1614718998L));
        stockPriceDtos.add(new StockPriceDto(355.0, 1615842198L));
        stockPriceDtos.add(new StockPriceDto(360.0, 1616533398L));
        return stockPriceDtos;
    }

    public static StockInfoDto getStockInfoDto() {
        return StockInfoDto.builder()
                .companyName("Tesla")
                .stockSymbol("TSLA")
                .dayProfit(6.0)
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

    public static PortfolioSummaryDto getPortfolioSummary() {
        List<SummaryStockInfoDto> summaryInfos = new ArrayList<>();
        summaryInfos.add(new SummaryStockInfoDto("TSLA", 5, 500.0, 5.0, 10.0, 100.0, 50.0));
        summaryInfos.add(new SummaryStockInfoDto("FB", 2, 50.0, 3.0, 4.0, 50.0, 20.0));
        summaryInfos.add(new SummaryStockInfoDto("AAPL", 4, 170.0, -1.0, -3.0, 80.0, 30.0));
        return new PortfolioSummaryDto(5000.0, 15.0, summaryInfos);
    }

    public static List<SummaryStockInfoDto> getStockSummaries() {
        List<SummaryStockInfoDto> infos = new ArrayList<>();
        infos.add(new SummaryStockInfoDto("TSLA", 4, 500.0, 4.0, 10.0, 20.0, 40.0));
        return infos;
    }
}
