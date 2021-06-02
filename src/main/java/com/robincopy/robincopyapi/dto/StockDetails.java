package com.robincopy.robincopyapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockDetails {

    private Long marketCapitalization;
    private String weburl;
    private String finnhubIndustry;
    private String name;
    private String stockSymbol;

    public String getDescription() {
        return name + " is a " + finnhubIndustry + " company." + " It's official site is " + weburl;
    }
}
