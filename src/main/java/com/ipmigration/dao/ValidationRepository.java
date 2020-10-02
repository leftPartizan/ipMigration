package com.ipmigration.dao;

import com.ipmigration.model.Validation;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ValidationRepository extends CrudRepository<Validation, Integer> {

    @Override
    List<Validation> findAll();
}
