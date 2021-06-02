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
public class StockSummary {

    private Double balance;
    private Double increasePercentage;
    private List<StockSummaryInfo> stocksInfo;

}
