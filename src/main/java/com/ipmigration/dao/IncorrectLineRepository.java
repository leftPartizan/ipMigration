package com.ipmigration.dao;

import com.ipmigration.model.IncorrectLine;
import org.springframework.data.repository.CrudRepository;

public interface IncorrectLineRepository extends CrudRepository<IncorrectLine, Integer> {

    @Override
    <S extends IncorrectLine> Iterable<S> saveAll(Iterable<S> iterable);
}
