package com.robincopy.robincopyapi.repositories;

import com.robincopy.robincopyapi.dto.api.StockInfo;
import com.robincopy.robincopyapi.dto.api.StockHistoricalDetails;
import com.robincopy.robincopyapi.dto.api.StockQuote;
import com.robincopy.robincopyapi.exceptions.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class StockInfoRepository implements StockRepository {

    @Value("${FINNHUB_TOKEN}")
    String token;

    public StockInfo getStockDetails(String stockSymbol) {
        RestTemplate restTemplate = new RestTemplate();
        final String url = String.format("https://finnhub.io/api/v1/stock/profile2?symbol=%s", stockSymbol);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Finnhub-Token", token);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<StockInfo> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, StockInfo.class);
        if (response.getStatusCodeValue() != 200) throw new BadRequestException("Server error");
        response.getBody().setStockSymbol(stockSymbol);
        return response.getBody();
    }

    public StockHistoricalDetails getStockInfo(String stockSymbol, long from, long to, String resolution){
        RestTemplate restTemplate = new RestTemplate();
        final String url = "https://finnhub.io/api/v1/stock/candle?symbol=" + stockSymbol + "&resolution=" + resolution + "&from=" + from + "&to=" + to;
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Finnhub-Token", token);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<StockHistoricalDetails> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, StockHistoricalDetails.class);
        if (response.getStatusCodeValue() != 200) throw new BadRequestException("Server error");

        return response.getBody();
    }

    public StockQuote getStockQuote(String stockSymbol) {
        RestTemplate restTemplate = new RestTemplate();
        final String url = String.format("https://finnhub.io/api/v1/quote?symbol=%s", stockSymbol);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Finnhub-Token", token);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        ResponseEntity<StockQuote> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, StockQuote.class);
        if (response.getStatusCodeValue() != 200) throw new BadRequestException("Server error");

        return response.getBody();
    }
}
