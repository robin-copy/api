package com.robincopy.robincopyapi.services;

import com.robincopy.robincopyapi.dto.NewShareDto;
import com.robincopy.robincopyapi.dto.ShareAddedDto;
import com.robincopy.robincopyapi.dto.UserDto;
import com.robincopy.robincopyapi.exceptions.BadRequestException;
import com.robincopy.robincopyapi.exceptions.NotFoundException;
import com.robincopy.robincopyapi.models.Share;
import com.robincopy.robincopyapi.models.User;
import com.robincopy.robincopyapi.repositories.ShareRepository;
import com.robincopy.robincopyapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final ShareRepository shareRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserService(ShareRepository shareRepository, UserRepository userRepository) {
        this.shareRepository = shareRepository;
        this.userRepository = userRepository;
    }

    public ShareAddedDto addShare(NewShareDto newShareDto) {
        User user = userRepository.findById(newShareDto.getUserId()).orElseThrow(() -> new NotFoundException("User not found"));
        Share share = new Share(newShareDto.getQuantity(), user, newShareDto.getSymbol());
        return shareRepository.save(share).toDto();
    }

    public UserDto addUser(UserDto userDto) {
        if(userRepository.existsByFirstNameAndLastName(userDto.getFirstName(), userDto.getLastName())) throw new BadRequestException("User already exists");
        userRepository.save(User.from(userDto));
        return userDto;
    }
}
