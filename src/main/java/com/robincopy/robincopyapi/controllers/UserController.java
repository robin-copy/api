package com.robincopy.robincopyapi.controllers;

import com.robincopy.robincopyapi.dto.*;
import com.robincopy.robincopyapi.dto.share.BuyShareDto;
import com.robincopy.robincopyapi.dto.share.ShareDto;
import com.robincopy.robincopyapi.dto.user.UserDto;
import com.robincopy.robincopyapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController(value = "UserController")
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserDto userDto){
        return userService.addUser(userDto);
    }

    @PostMapping("/shares")
    public ShareDto buyShare(@RequestBody @Valid BuyShareDto buyShareDto){
        return userService.buyShare(buyShareDto);
    }

    @GetMapping("/{userId}/shares")
    public List<StockReducedInfoDto> getUserStocksInfo(@PathVariable(value = "userId") String userId){
        return userService.getUserStockInfo(userId);
    }

    @GetMapping("/{userId}/shares/{stockSymbol}")
    public StockInfoDto getUserStockInfo(@PathVariable(value = "userId") String userId, @PathVariable(value = "stockSymbol") String stockSymbol){
        return userService.getUserStockInfo(userId, stockSymbol);
    }

    @GetMapping("/{userId}/summary")
    public PortfolioSummaryDto getUserStocksSummary(@PathVariable(value = "userId") String userId){
        return userService.getUserStockSummary(userId);
    }

    @GetMapping("/defaultUser")
    public String getDefaultUser(){
        return userService.getDefaultUserId();
    }
}
