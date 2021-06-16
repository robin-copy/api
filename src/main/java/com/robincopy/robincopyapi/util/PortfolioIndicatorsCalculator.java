package com.robincopy.robincopyapi.util;

import com.robincopy.robincopyapi.dto.*;
import com.robincopy.robincopyapi.dto.api.StockHistoricalDetails;
import com.robincopy.robincopyapi.dto.api.StockInfo;
import com.robincopy.robincopyapi.dto.api.StockQuote;
import com.robincopy.robincopyapi.exceptions.NotFoundException;
import com.robincopy.robincopyapi.models.PriceStatus;
import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import com.robincopy.robincopyapi.repositories.StockRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PortfolioIndicatorsCalculator {

    public static StockInfoDto getStockInfo(Share share, StockHistoricalDetails stockHistoricalDetails, StockInfo stockInfo, StockQuote stockQuote) {
        List<Double> closePrices = stockHistoricalDetails.getC();
        Double profit = getProfit(share.getAverageBuyPrice(), share.getQuantity(), stockQuote.getC());
        return StockInfoDto.builder()
                .stockSymbol(share.getStockSymbol())
                .companyName(stockInfo.getName())
                .companyDescription(stockInfo.getDescription())
                .dayProfit((stockQuote.getC() - stockQuote.getO()) * share.getQuantity())
                .dayVariationPercentage(getVariationPercentage(stockQuote.getC(), stockQuote.getPc()))
                .profit(profit)
                .profitPercentage(getAvgProfitPercentage(share.getAverageBuyPrice(), stockQuote.getC()))
                .price(stockQuote.getC())
                .stockPrices(getStockPrices(stockHistoricalDetails, stockHistoricalDetails.getC().size()))
                .openValue(stockQuote.getO())
                .dayHigh(stockQuote.getH())
                .dayLow(stockQuote.getL())
                .yearHigh(stockHistoricalDetails.getH().stream().max(Comparator.naturalOrder()).orElseThrow(() -> new NotFoundException("Year high not found!")))
                .yearLow(stockHistoricalDetails.getH().stream().min(Comparator.naturalOrder()).orElseThrow(() -> new NotFoundException("Year low not found!")))
                .avgVolume(stockHistoricalDetails.getV().stream().reduce(0.0, Double::sum) / stockHistoricalDetails.getV().size())
                .peRatio(getPeRatio(stockQuote.getC(), profit))
                .divYield(getDividendYield(share.getAverageBuyPrice(), stockQuote.getC(), closePrices.get(closePrices.size() - 1)))
                .marketCap(stockInfo.getMarketCapitalization())
                .build();
    }

    public static StockReducedInfoDto getStockReducedInfo(Share share, int length, StockHistoricalDetails stockHistoricalDetails, StockQuote stockQuote) {
        PriceStatus status = stockQuote.getO() > stockQuote.getC() ? PriceStatus.DECREASED :
                (stockQuote.getO().equals(stockQuote.getC()) ? PriceStatus.EQUAL : PriceStatus.INCREASED);
        return StockReducedInfoDto.builder()
                .stockSymbol(share.getStockSymbol())
                .price(stockQuote.getC())
                .stockPriceDtos(getStockPrices(stockHistoricalDetails, length))
                .sharesQuantity(share.getQuantity())
                .priceStatus(status)
                .build();
    }

    public static PortfolioSummaryDto getPortfolioSummaryDto(User user, StockRepository stockRepository) {
        List<Share> userShares = user.getShares();

        Double balance = userShares.stream().map(share -> {
            StockQuote stockQuote = stockRepository.getStockQuote(share.getStockSymbol());
            return share.getQuantity() * stockQuote.getC();
        }).reduce(0.0, Double::sum);

        return PortfolioSummaryDto.builder()
                .balance(balance)
                .increasePercentage(getAvgProfitPercentage(user.getInvestedBalance(), balance))
                .stocksInfo(userShares.stream().map(share -> getUserStockSummaryInfo(share, stockRepository.getStockQuote(share.getStockSymbol())))
                        .collect(Collectors.toList()))
                .build();
    }

    private static List<StockPriceDto> getStockPrices(StockHistoricalDetails stockHistoricalDetails, int quantity) {
        List<StockPriceDto> stockPriceDtos = new ArrayList<>();
        for (int i = stockHistoricalDetails.getC().size() - 1; i >= stockHistoricalDetails.getC().size() - quantity; i--) {
            stockPriceDtos.add(new StockPriceDto(stockHistoricalDetails.getC().get(i), stockHistoricalDetails.getT().get(i)));
        }
        return stockPriceDtos;
    }

    private static SummaryStockInfoDto getUserStockSummaryInfo(Share share, StockQuote stockQuote) {
        double dailyVariationPercentage = getVariationPercentage(stockQuote.getC(), stockQuote.getPc());
        double totalVariationPercentage = getVariationPercentage(stockQuote.getC(), share.getAverageBuyPrice());
        return SummaryStockInfoDto.builder()
                .stockSymbol(share.getStockSymbol())
                .quantity(share.getQuantity())
                .lastPrice(stockQuote.getC())
                .dailyVariationPercentage(dailyVariationPercentage)
                .dailyVariation(dailyVariationPercentage * (stockQuote.getC()) / 100)
                .totalVariation(totalVariationPercentage)
                .totalWining(totalVariationPercentage * (share.getAverageBuyPrice() * share.getQuantity()) / 100)
                .build();
    }

    private static double getAvgProfitPercentage(Double buyPrice, Double actualValue) {
        return (100 * actualValue / buyPrice) - 100.0;
    }

    private static double getVariationPercentage(Double actualPrice, Double pc) {
        return ((actualPrice - pc) / actualPrice) * 100;
    }

    private static double getProfit(Double averagePrice, int quantity, Double actualPrice) {
        return (actualPrice - averagePrice) * quantity;
    }

    private static double getDividendYield(Double averagePrice, Double actualPrice, Double closePrice) {
        return averagePrice - closePrice / actualPrice;
    }

    private static double getPeRatio(Double actualPrice, Double profit) {
        return actualPrice / profit;
    }

}
