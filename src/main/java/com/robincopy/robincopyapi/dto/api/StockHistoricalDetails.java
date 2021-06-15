package com.robincopy.robincopyapi.dto.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StockHistoricalDetails {

    List<Double> c;
    List<Double> h;
    List<Double> l;
    List<Double> o;
    List<Long> t;
    String s;
    List<Double> v;
}
