package com.robincopy.robincopyapi.services;

import com.robincopy.robincopyapi.dto.*;
import com.robincopy.robincopyapi.exceptions.BadRequestException;
import com.robincopy.robincopyapi.exceptions.NotFoundException;
import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import com.robincopy.robincopyapi.repositories.StockInfoRepository;
import com.robincopy.robincopyapi.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final StockInfoRepository stockRepository;
    private final StockService stockService;
    private static Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    public UserService(UserRepository userRepository, StockInfoRepository stockRepository, StockService stockService) {
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
        this.stockService = stockService;
    }

    public ShareAddedDto addShare(NewShareDto newShareDto) {
        User user = userRepository.findById(newShareDto.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
        Double price = stockRepository.getStockQuote(newShareDto.getSymbol()).getC();
        Share share = new Share(newShareDto.getQuantity(), user, newShareDto.getSymbol(), price);
        user.addShare(share.getQuantity(), price, share.getStockSymbol());
        userRepository.save(user);
        return share.toDto();
    }

    public UserDto addUser(UserDto userDto) {
        if(userRepository.existsByFirstNameAndLastName(userDto.getFirstName(), userDto.getLastName())) throw new BadRequestException("User already exists");
        User user = userRepository.save(User.from(userDto));
        userDto.setUserId(user.getId());
        return userDto;
    }

    public List<StockReducedInfo> getUserStockInfo(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return user.getShares().stream().map(share -> stockService.generateReducedStockInfo(share, "D", 10)).collect(Collectors.toList());
    }

    public StockInfo getUserStockInfo(String userId, String stockSymbol){
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        Share foundShare = user.getShares().stream().filter(share ->
                share.getStockSymbol().equals(stockSymbol)).findFirst().orElseThrow(() -> new NotFoundException("User doesn't have that stock"));
        return stockService.generateStockInfo(foundShare,"D");
    }

    public StockSummary getUserStockSummary(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        return stockService.getUserStockSummary(user);
    }

    @PostConstruct
    private void init() {
        if (!userRepository.existsByFirstNameAndLastName("Juan", "Perez")) {
            User user = new User("Juan", "Perez");
            user.addShare(4, 200.0, "TSLA");
            user.addShare(5, 500.0, "FB");
            user.addShare(2, 1000.0, "AAPL");
            user = userRepository.save(user);
            logger.info("Created user with id: " + user.getId());
        }
    }
}
