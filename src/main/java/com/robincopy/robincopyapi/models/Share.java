package com.robincopy.robincopyapi.models;

import com.robincopy.robincopyapi.dto.ShareAddedDto;
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

    @OneToOne(fetch = FetchType.LAZY)
    private User holder;

    private String stockSymbol;

    public ShareAddedDto toDto() {
        return ShareAddedDto.builder()
                .quantity(quantity)
                .symbol(stockSymbol)
                .userId(holder.getId())
                .build();
    }
}
