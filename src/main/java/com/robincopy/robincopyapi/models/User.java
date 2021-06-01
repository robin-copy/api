package com.robincopy.robincopyapi.models;

import com.robincopy.robincopyapi.dto.UserDto;
import com.robincopy.robincopyapi.exceptions.BadRequestException;
import com.robincopy.robincopyapi.exceptions.NotFoundException;
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

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.shares = new ArrayList<>();
    }

    public static User from(UserDto userDto) {
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .build();
    }

    public void addShare(int quantity, Double price, String stockSymbol) {
        Optional<Share> share = shares.stream().filter(share1 -> share1.getStockSymbol().equals(stockSymbol)).findFirst();
        if (share.isPresent()) share.get().increaseQuantity(quantity, price);
        else shares.add(new Share(quantity, this, stockSymbol, price));
    }

    public void removeShare(String stockSymbol, int quantity) {
        Share share = shares.stream().filter(share1 -> share1.getStockSymbol().equals(stockSymbol)).findFirst().orElseThrow(() -> new NotFoundException("Share not found"));
        if (share.getQuantity() - quantity > 0) share.decreaseQuantity(quantity);
        else if (share.getQuantity() - quantity == 0) shares.remove(share);
        else throw new BadRequestException("Not enough shares");
    }

    public Double getAverageSharePrice(String shareId) {
        Share found = shares.stream().filter(share -> share.getId().equals(shareId)).findFirst().orElseThrow(() -> new NotFoundException("Share not found"));
        return found.getAverageBuyPrice();
    }

    public Share getShare(String shareId){
        return shares.stream().filter(share -> share.getId().equals(shareId)).findFirst().orElseThrow(() -> new NotFoundException("Share not found"));
    }
}
