package com.robincopy.robincopyapi.dto;

import com.robincopy.robincopyapi.models.PriceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockReducedInfoDto {

    private String stockSymbol;
    private Double price;
    private List<StockPriceDto> stockPriceDtos;
    private int sharesQuantity;
    private PriceStatus priceStatus;
}
