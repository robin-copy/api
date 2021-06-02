package com.robincopy.robincopyapi.repositories;

import com.robincopy.robincopyapi.models.Share;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareRepository extends CrudRepository<Share, String> {

}
