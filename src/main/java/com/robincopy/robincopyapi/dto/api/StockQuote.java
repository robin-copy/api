package com.robincopy.robincopyapi.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockQuote {

    //current price
    private Double c;

    //day high
    private Double h;

    //day low
    private Double l;

    //day open
    private Double o;

    //previous close price
    private Double pc;

    //time
    private Long t;
}
