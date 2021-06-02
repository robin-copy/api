package com.robincopy.robincopyapi.services;

import com.robincopy.robincopyapi.dto.*;
import com.robincopy.robincopyapi.exceptions.NotFoundException;
import com.robincopy.robincopyapi.models.PriceStatus;
import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import com.robincopy.robincopyapi.repositories.StockInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockService {

    private final StockInfoRepository stockInfoRepository;
    static final long MILLISECONDS_IN_YEAR = (long) 1000 * 60 * 60 * 24 * 365;

    @Autowired
    public StockService(StockInfoRepository stockInfoRepository) {
        this.stockInfoRepository = stockInfoRepository;
    }

    public StockInfo generateStockInfo(Share share, String resolution) {
        long endTime = System.currentTimeMillis() / 1000L;
        long startTime = (System.currentTimeMillis() - MILLISECONDS_IN_YEAR) / 1000L;
        StockInfoDto stockInfoDto = stockInfoRepository.getStockInfo(share.getStockSymbol(), startTime, endTime, resolution);
        StockDetails stockDetails = stockInfoRepository.getStockDetails(share.getStockSymbol());
        StockQuoteInfo stockQuoteInfo = stockInfoRepository.getStockQuote(share.getStockSymbol());
        List<Double> closePrices = stockInfoDto.getC();
        Double dayVariationPercentage = ((stockQuoteInfo.getC() - stockQuoteInfo.getPc()) / stockQuoteInfo.getC()) * 100;
        Double avgProfitPercentage = (100 * stockQuoteInfo.getC() / share.getAverageBuyPrice()) - 100.0;
        Double profit = (stockQuoteInfo.getC() - share.getAverageBuyPrice()) * share.getQuantity();
        return StockInfo.builder()
                .stockSymbol(share.getStockSymbol())
                .companyName(stockDetails.getName())
                .companyDescription(stockDetails.getDescription())
                .dayProfit((stockQuoteInfo.getC() - stockQuoteInfo.getO()) * share.getQuantity())
                .dayVariationPercentage(dayVariationPercentage)
                .profit(profit)
                .profitPercentage(avgProfitPercentage)
                .price(stockQuoteInfo.getC())
                .stockPrices(getStockPrices(stockInfoDto, stockInfoDto.getC().size()))
                .openValue(stockQuoteInfo.getO())
                .dayHigh(stockQuoteInfo.getH())
                .dayLow(stockQuoteInfo.getL())
                .yearHigh(stockInfoDto.getH().stream().max(Comparator.naturalOrder()).orElseThrow(() -> new NotFoundException("Year high not found!")))
                .yearLow(stockInfoDto.getH().stream().min(Comparator.naturalOrder()).orElseThrow(() -> new NotFoundException("Year low not found!")))
                .avgVolume(stockInfoDto.getV().stream().reduce(0.0, Double::sum) / stockInfoDto.getV().size())
                .peRatio(stockQuoteInfo.getC() / profit)
                .divYield(share.getAverageBuyPrice() - closePrices.get(closePrices.size() - 1) / stockQuoteInfo.getC())
                .marketCap(stockDetails.getMarketCapitalization())
                .build();
    }

    public StockReducedInfo generateReducedStockInfo(Share share, String resolution, int length) {
        long endTime = System.currentTimeMillis() / 1000L;
        long startTime = (System.currentTimeMillis() - MILLISECONDS_IN_YEAR) / 1000L;
        StockInfoDto stockInfoDto = stockInfoRepository.getStockInfo(share.getStockSymbol(), startTime, endTime, resolution);
        StockQuoteInfo stockQuoteInfo = stockInfoRepository.getStockQuote(share.getStockSymbol());
        PriceStatus status = stockQuoteInfo.getO() > stockQuoteInfo.getC() ? PriceStatus.DECREASED : (stockQuoteInfo.getO().equals(stockQuoteInfo.getC()) ? PriceStatus.EQUAL : PriceStatus.INCREASED);
        return StockReducedInfo.builder()
                .stockSymbol(share.getStockSymbol())
                .price(stockQuoteInfo.getC())
                .stockPrices(getStockPrices(stockInfoDto, length))
                .sharesQuantity(share.getQuantity())
                .priceStatus(status)
                .build();
    }

    public StockSummary getUserStockSummary(User user) {
        List<Share> userShares = user.getShares();
        Double balance = userShares.stream().map(share -> {
            StockQuoteInfo stockQuoteInfo = stockInfoRepository.getStockQuote(share.getStockSymbol());
            return share.getQuantity() * stockQuoteInfo.getC();
        }).reduce(0.0, Double::sum);
        return StockSummary.builder()
                .balance(balance)
                .increasePercentage((balance * 100 / user.getInvestedBalance()) - 100)
                .stocksInfo(userShares.stream().map(this::getUserStockSummaryInfo).collect(Collectors.toList()))
                .build();
    }

    private StockSummaryInfo getUserStockSummaryInfo(Share share) {
        StockQuoteInfo stockQuoteInfo = stockInfoRepository.getStockQuote(share.getStockSymbol());
        double dailyVariationPercentage = ((stockQuoteInfo.getC() - stockQuoteInfo.getPc()) / stockQuoteInfo.getC()) * 100;
        double totalVariationPercentage = ((stockQuoteInfo.getC() - share.getAverageBuyPrice()) / stockQuoteInfo.getC()) * 100;
        return StockSummaryInfo.builder()
                .stockSymbol(share.getStockSymbol())
                .quantity(share.getQuantity())
                .lastPrice(stockQuoteInfo.getC())
                .dailyVariationPercentage(dailyVariationPercentage)
                .dailyVariation(dailyVariationPercentage * (stockQuoteInfo.getC()) / 100)
                .totalVariation(totalVariationPercentage)
                .totalWining(totalVariationPercentage * (share.getAverageBuyPrice() * share.getQuantity()) / 100)
                .build();
    }

    private List<StockPrice> getStockPrices(StockInfoDto stockInfoDto, int quantity) {
        List<StockPrice> stockPrices = new ArrayList<>();
        for (int i = stockInfoDto.getC().size() - 1; i >= stockInfoDto.getC().size() - quantity; i--) {
            stockPrices.add(new StockPrice(stockInfoDto.getC().get(i), stockInfoDto.getT().get(i)));
        }
        return stockPrices;
    }

}
