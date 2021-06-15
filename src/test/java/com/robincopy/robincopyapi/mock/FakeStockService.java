package com.robincopy.robincopyapi.mock;

import com.robincopy.robincopyapi.dto.StockInfoDto;
import com.robincopy.robincopyapi.dto.StockReducedInfoDto;
import com.robincopy.robincopyapi.dto.PortfolioSummaryDto;
import com.robincopy.robincopyapi.dto.api.StockQuote;
import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import com.robincopy.robincopyapi.services.StockService;

public class FakeStockService implements StockService {

    @Override
    public StockInfoDto getStockInfo(Share share, String resolution) {
        return StockMocks.getStockInfoDto();
    }

    @Override
    public StockReducedInfoDto getReducedStockInfo(Share share, String resolution, int length) {
        return StockMocks.getStockReducedInfos().get(0);
    }

    @Override
    public PortfolioSummaryDto getUserStockSummary(User user) {
        return StockMocks.getPortfolioSummary();
    }

    @Override
    public StockQuote getStockQuote(String symbol) {
        return StockMocks.getStockQuote();
    }
}
