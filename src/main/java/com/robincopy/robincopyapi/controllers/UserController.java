package com.robincopy.robincopyapi.controllers;

import com.robincopy.robincopyapi.dto.NewShareDto;
import com.robincopy.robincopyapi.dto.ShareAddedDto;
import com.robincopy.robincopyapi.dto.UserDto;
import com.robincopy.robincopyapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/shares")
    public ShareAddedDto addShare(@RequestBody @Valid NewShareDto newShareDto){
        return userService.addShare(newShareDto);
    }

    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserDto userDto){
        return userService.addUser(userDto);
    }
}
