package com.robincopy.robincopyapi.mock;

import com.robincopy.robincopyapi.dto.*;
import com.robincopy.robincopyapi.dto.share.BuyShareDto;
import com.robincopy.robincopyapi.dto.share.ShareDto;
import com.robincopy.robincopyapi.dto.user.UserDto;
import com.robincopy.robincopyapi.services.UserService;

import java.util.List;

public class FakeUserService implements UserService {

    @Override
    public ShareDto buyShare(BuyShareDto buyShareDto) {
        return new ShareDto(buyShareDto.getUserId(), buyShareDto.getSymbol(), buyShareDto.getQuantity(), 500.0);
    }

    @Override
    public UserDto addUser(UserDto userDto) {
        return userDto;
    }

    @Override
    public List<StockReducedInfoDto> getUserStockInfo(String userId) {
        return StockMocks.getStockReducedInfos();
    }

    @Override
    public StockInfoDto getUserStockInfo(String userId, String stockSymbol) {
        return StockMocks.getStockInfoDto();
    }

    @Override
    public PortfolioSummaryDto getUserStockSummary(String userId) {
        return StockMocks.getPortfolioSummary();
    }

    @Override
    public String getDefaultUserId() {
        return "fake";
    }
}
