package com.robincopy.robincopyapi.repositories;

import com.robincopy.robincopyapi.dto.StockDetails;
import com.robincopy.robincopyapi.dto.StockInfoDto;
import com.robincopy.robincopyapi.dto.StockQuoteInfo;
import com.robincopy.robincopyapi.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class StockInfoRepository {

    @Value("${FINNHUB_TOKEN}")
    String token;

    public StockDetails getStockDetails(String stockSymbol) {
        RestTemplate restTemplate = new RestTemplate();
        final String url = String.format("https://finnhub.io/api/v1/stock/profile2?symbol=%s", stockSymbol);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Finnhub-Token", token);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<StockDetails> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, StockDetails.class);
        if (response.getStatusCodeValue() != 200) throw new BadRequestException("Server error");
        response.getBody().setStockSymbol(stockSymbol);
        return response.getBody();
    }

    public StockInfoDto getStockInfo(String stockSymbol, long from, long to, String resolution){
        RestTemplate restTemplate = new RestTemplate();
        final String url = "https://finnhub.io/api/v1/stock/candle?symbol=" + stockSymbol + "&resolution=" + resolution + "&from=" + from + "&to=" + to;
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Finnhub-Token", token);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<StockInfoDto> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, StockInfoDto.class);
        if (response.getStatusCodeValue() != 200) throw new BadRequestException("Server error");

        return response.getBody();
    }

    public StockQuoteInfo getStockQuote(String stockSymbol) {
        RestTemplate restTemplate = new RestTemplate();
        final String url = String.format("https://finnhub.io/api/v1/quote?symbol=%s", stockSymbol);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Finnhub-Token", token);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<StockQuoteInfo> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, StockQuoteInfo.class);
        if (response.getStatusCodeValue() != 200) throw new BadRequestException("Server error");

        return response.getBody();
    }
}
