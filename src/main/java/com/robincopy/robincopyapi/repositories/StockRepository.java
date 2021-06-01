package com.robincopy.robincopyapi.repositories;

import com.robincopy.robincopyapi.models.Stock;
import org.springframework.data.repository.CrudRepository;

public interface StockRepository extends CrudRepository<Stock, String> {
}
