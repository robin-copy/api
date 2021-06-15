package com.robincopy.robincopyapi.mock;

import com.robincopy.robincopyapi.dto.api.StockInfo;
import com.robincopy.robincopyapi.dto.api.StockHistoricalDetails;
import com.robincopy.robincopyapi.dto.api.StockQuote;
import com.robincopy.robincopyapi.repositories.StockRepository;

public class FakeStockInfoRepository implements StockRepository {
    @Override
    public StockInfo getStockDetails(String stockSymbol) {
        return StockMocks.getStockInfo();
    }

    @Override
    public StockHistoricalDetails getStockInfo(String stockSymbol, long from, long to, String resolution) {
        return StockMocks.getStockHistoricalDetails();
    }

    @Override
    public StockQuote getStockQuote(String stockSymbol) {
        return StockMocks.getStockQuote();
    }
}
