package com.robincopy.robincopyapi.services;

import com.robincopy.robincopyapi.dto.*;
import com.robincopy.robincopyapi.dto.share.BuyShareDto;
import com.robincopy.robincopyapi.dto.share.ShareDto;
import com.robincopy.robincopyapi.dto.user.UserDto;

import java.util.List;

public interface UserService {
    ShareDto buyShare(BuyShareDto buyShareDto);
    UserDto addUser(UserDto userDto);
    List<StockReducedInfoDto> getUserStockInfo(String userId);
    StockInfoDto getUserStockInfo(String userId, String stockSymbol);
    PortfolioSummaryDto getUserStockSummary(String userId);
    String getDefaultUserId();
}
