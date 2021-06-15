package com.robincopy.robincopyapi.repositories;

import com.robincopy.robincopyapi.models.Share;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShareRepository extends CrudRepository<Share, String> {
    Optional<Share> findByHolder_IdAndAndStockSymbol(String holderId, String symbol);

}
