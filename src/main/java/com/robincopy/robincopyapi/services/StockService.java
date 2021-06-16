package com.robincopy.robincopyapi.services;


import com.robincopy.robincopyapi.dto.StockInfoDto;
import com.robincopy.robincopyapi.dto.StockReducedInfoDto;
import com.robincopy.robincopyapi.dto.PortfolioSummaryDto;
import com.robincopy.robincopyapi.dto.api.StockQuote;
import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;

public interface StockService {
    StockInfoDto getStockInfo(Share share, String resolution);
    StockReducedInfoDto getReducedStockInfo(Share share, String resolution, int length);
    PortfolioSummaryDto getUserStockSummary(User user);
    StockQuote getStockQuote(String symbol);
}
