package com.robincopy.robincopyapi.repositories;

import com.robincopy.robincopyapi.dto.api.StockInfo;
import com.robincopy.robincopyapi.dto.api.StockHistoricalDetails;
import com.robincopy.robincopyapi.dto.api.StockQuote;

public interface StockRepository {

    StockInfo getStockDetails(String stockSymbol);
    StockHistoricalDetails getStockInfo(String stockSymbol, long from, long to, String resolution);
    StockQuote getStockQuote(String stockSymbol);
}

