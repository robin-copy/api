package com.robincopy.robincopyapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockInfoDto {

    private String companyName;
    private String stockSymbol;
    private Double dayProfit;
    private Double price;
    private List<StockPriceDto> stockPrices;
    private Double openValue;
    private Double dayHigh;
    private Double dayLow;
    private Double yearHigh;
    private Double yearLow;
    private Double avgVolume;
    private Double peRatio;
    private Double divYield;
    private Long marketCap;
    private String companyDescription;
    private Double profit;
    private Double profitPercentage;
    private Double dayVariationPercentage;
}
