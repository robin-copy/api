package com.robincopy.robincopyapi.models;

import com.robincopy.robincopyapi.dto.ShareAddedDto;
import com.robincopy.robincopyapi.exceptions.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "share")
@AllArgsConstructor
@NoArgsConstructor
public class Share extends AbstractEntity{

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    private User holder;

    private String stockSymbol;

    private Double averageBuyPrice;

    public ShareAddedDto toDto() {
        return new ShareAddedDto(holder.getId(), stockSymbol, quantity, averageBuyPrice);
    }

    public void increaseQuantity(int amount, Double price) {
        if(amount <= 0) throw new BadRequestException("Share amount parameter can't be less or equal to zero");
        averageBuyPrice = (averageBuyPrice * quantity + price * amount) / (quantity + amount);
        quantity+=amount;
    }

    public void decreaseQuantity(int quantity) {
        if(this.quantity - quantity < 0) throw new BadRequestException("Updated quantity can't be negative");
        if(this.quantity - quantity == 0) averageBuyPrice = 0.0;
        this.quantity-=quantity;
    }
}
