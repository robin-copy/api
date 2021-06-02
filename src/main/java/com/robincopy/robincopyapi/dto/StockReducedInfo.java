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
public class StockReducedInfo {

    private String stockSymbol;
    private Double price;
    private List<StockPrice> stockPrices;
    private int sharesQuantity;
    private PriceStatus priceStatus;
}
