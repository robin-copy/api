package com.robincopy.robincopyapi.controllers;

import com.robincopy.robincopyapi.dto.*;
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
    public ShareAddedDto addShare(@RequestBody @Valid NewShareDto newShareDto){
        return userService.addShare(newShareDto);
    }

    @GetMapping("/{userId}/shares")
    public List<StockReducedInfo> getUserStocksInfo(@PathVariable(value = "userId") String userId){
        return userService.getUserStockInfo(userId);
    }

    @GetMapping("/{userId}/shares/{stockSymbol}")
    public StockInfo getUserStockInfo(@PathVariable(value = "userId") String userId, @PathVariable(value = "stockSymbol") String stockSymbol){
        return userService.getUserStockInfo(userId, stockSymbol);
    }

    @GetMapping("/{userId}/summary")
    public StockSummary getUserStocksSummary(@PathVariable(value = "userId") String userId){
        return userService.getUserStockSummary(userId);
    }
}
