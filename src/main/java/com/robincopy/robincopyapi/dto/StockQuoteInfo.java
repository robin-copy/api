package com.robincopy.robincopyapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockQuoteInfo {

    private Double c;
    private Double h;
    private Double l;
    private Double o;
    private Double pc;
    private Long t;
}
