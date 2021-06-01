package com.robincopy.robincopyapi.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/stocks")
public class StockController {

    @GetMapping("")
    public String getAllStocks() {
        return "";
    }

}
