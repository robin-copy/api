package com.robincopy.robincopyapi.services.impl;

import com.robincopy.robincopyapi.dto.*;
import com.robincopy.robincopyapi.dto.api.StockInfo;
import com.robincopy.robincopyapi.dto.api.StockHistoricalDetails;
import com.robincopy.robincopyapi.dto.api.StockQuote;
import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import com.robincopy.robincopyapi.repositories.MockedRepository;
import com.robincopy.robincopyapi.repositories.StockRepository;
import com.robincopy.robincopyapi.services.StockService;
import com.robincopy.robincopyapi.util.PortfolioIndicatorsCalculator;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockInfoRepository;

    private MockedRepository mockedRepository;

    static final long MILLISECONDS_IN_YEAR = (long) 1000 * 60 * 60 * 24 * 365;


    @Value("${MOCKED_EXTERNAL_API}")
    private boolean mocked;

    @Autowired
    public StockServiceImpl(StockRepository stockInfoRepository) {
        this.stockInfoRepository = stockInfoRepository;
        this.mockedRepository = new MockedRepository();
    }

    @Override
    public StockInfoDto getStockInfo(Share share, String resolution) {
        long endTime = System.currentTimeMillis() / 1000L;
        long startTime = (System.currentTimeMillis() - MILLISECONDS_IN_YEAR) / 1000L;
        StockHistoricalDetails stockHistoricalDetails = getStockRepository().getStockInfo(share.getStockSymbol(), startTime, endTime, resolution);
        StockInfo stockInfo = getStockRepository().getStockDetails(share.getStockSymbol());
        StockQuote stockQuote = getStockRepository().getStockQuote(share.getStockSymbol());
        return PortfolioIndicatorsCalculator.getStockInfo(share, stockHistoricalDetails, stockInfo, stockQuote);
    }

    @Override
    public StockReducedInfoDto getReducedStockInfo(Share share, String resolution, int length) {
        long endTime = System.currentTimeMillis() / 1000L;
        long startTime = (System.currentTimeMillis() - MILLISECONDS_IN_YEAR) / 1000L;
        StockHistoricalDetails stockHistoricalDetails = getStockRepository().getStockInfo(share.getStockSymbol(), startTime, endTime, resolution);
        StockQuote stockQuote = getStockRepository().getStockQuote(share.getStockSymbol());
        return PortfolioIndicatorsCalculator.getStockReducedInfo(share, length, stockHistoricalDetails, stockQuote);
    }

    @Override
    public PortfolioSummaryDto getUserStockSummary(User user) {
        return PortfolioIndicatorsCalculator.getPortfolioSummaryDto(user, getStockRepository());
    }

    @Override
    public StockQuote getStockQuote(String symbol) {
        return getStockRepository().getStockQuote(symbol);
    }

    private StockRepository getStockRepository(){
        return mocked ? mockedRepository : stockInfoRepository;
    }

}
