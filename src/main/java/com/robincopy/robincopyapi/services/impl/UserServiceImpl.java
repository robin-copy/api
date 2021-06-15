package com.robincopy.robincopyapi.services.impl;

import com.robincopy.robincopyapi.dto.*;
import com.robincopy.robincopyapi.dto.share.BuyShareDto;
import com.robincopy.robincopyapi.dto.share.ShareDto;
import com.robincopy.robincopyapi.dto.user.UserDto;
import com.robincopy.robincopyapi.exceptions.NotFoundException;
import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import com.robincopy.robincopyapi.repositories.UserRepository;
import com.robincopy.robincopyapi.services.StockService;
import com.robincopy.robincopyapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StockService stockService;
    private static final Integer DEFAULT_STOCK_LIST_SIZE = 10;
    private static final String DEFAULT_STOCK_INFO_RESOLUTION = "D";
    private static final String DEFAULT_USER_NAME = "Juan";
    private static final String DEFAULT_USER_LASTNAME = "Perez";
    private static final String DEFAULT_USER_NOT_FOUND_MESSAGE = "Perez";

    @Autowired
    public UserServiceImpl(UserRepository userRepository, StockService stockService) {
        this.userRepository = userRepository;
        this.stockService = stockService;
    }

    public ShareDto buyShare(BuyShareDto buyShareDto) {
        User user = userRepository.findById(buyShareDto.getUserId()).orElseThrow(() -> new NotFoundException(DEFAULT_USER_NOT_FOUND_MESSAGE));
        Double price = stockService.getStockQuote(buyShareDto.getSymbol()).getC();

        Share share = Share.from(buyShareDto, price, user);
        user.buyShare(share);
        userRepository.save(user);
        return share.toDto();
    }

    public UserDto addUser(UserDto userDto) {
        User user = userRepository.save(User.from(userDto, userRepository));
        userDto.setUserId(user.getId());
        return userDto;
    }

    public List<StockReducedInfoDto> getUserStockInfo(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(DEFAULT_USER_NOT_FOUND_MESSAGE));
        return user.getShares().stream().map(share -> stockService.getReducedStockInfo(share, DEFAULT_STOCK_INFO_RESOLUTION, DEFAULT_STOCK_LIST_SIZE)).collect(Collectors.toList());
    }

    public StockInfoDto getUserStockInfo(String userId, String stockSymbol){
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(DEFAULT_USER_NOT_FOUND_MESSAGE));
        Share foundShare = user.getShares().stream().filter(share ->
                share.getStockSymbol().equals(stockSymbol)).findFirst().orElseThrow(() -> new NotFoundException("User doesn't have that stock"));
        return stockService.getStockInfo(foundShare,DEFAULT_STOCK_INFO_RESOLUTION);
    }

    public PortfolioSummaryDto getUserStockSummary(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(DEFAULT_USER_NOT_FOUND_MESSAGE));
        return stockService.getUserStockSummary(user);
    }


    public String getDefaultUserId(){
        User defaultUser = userRepository.findByFirstNameAndLastName(DEFAULT_USER_NAME, DEFAULT_USER_LASTNAME).orElseThrow(() -> new NotFoundException(DEFAULT_USER_NOT_FOUND_MESSAGE));
        return defaultUser.getId();
    }
}
