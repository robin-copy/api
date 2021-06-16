package com.robincopy.robincopyapi.models;

import com.robincopy.robincopyapi.dto.user.UserDto;
import com.robincopy.robincopyapi.exceptions.BadRequestException;
import com.robincopy.robincopyapi.exceptions.NotFoundException;
import com.robincopy.robincopyapi.repositories.UserRepository;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "user_data")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends AbstractEntity {

    private String firstName;

    private String lastName;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "holder")
    private List<Share> shares;

    private Double investedBalance;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.shares = new ArrayList<>();
        this.investedBalance = 0.0;
    }

    public static User from(UserDto userDto, UserRepository userRepository) {
        if(userRepository.existsByFirstNameAndLastName(userDto.getFirstName(), userDto.getLastName())) throw new BadRequestException("User already exists");

        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .investedBalance(0.0)
                .build();
    }

    public void buyShare(Share share) {
        investedBalance += (share.getAverageBuyPrice() * share.getQuantity());
        Optional<Share> found = findShareBySymbol(share.getStockSymbol());
        if (found.isPresent()) found.get().increaseQuantity(share.getQuantity(), share.getAverageBuyPrice());
        else shares.add(share);
    }

    public void sellShare(Share share) {
        Share toRemove = findShareBySymbol(share.getStockSymbol()).orElseThrow(() -> new NotFoundException("Share not found"));
        if (toRemove.getQuantity() - share.getQuantity() > 0) toRemove.decreaseQuantity(share.getQuantity());
        else if (toRemove.getQuantity() - share.getQuantity() == 0) shares.remove(toRemove);
        else throw new BadRequestException("Not enough shares");
    }

    private Optional<Share> findShareBySymbol(String symbol) {
        return shares.stream().filter(share1 -> share1.getStockSymbol().equals(symbol)).findFirst();
    }
}
