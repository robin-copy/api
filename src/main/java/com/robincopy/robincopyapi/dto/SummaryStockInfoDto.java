package com.robincopy.robincopyapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SummaryStockInfoDto {

    private String stockSymbol;
    private Integer quantity;
    private Double lastPrice;
    private Double dailyVariationPercentage;
    private Double dailyVariation;
    private Double totalVariation;
    private Double totalWining;

}
