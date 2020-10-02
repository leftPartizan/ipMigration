package com.ipmigration.dao;

import com.ipmigration.model.Router;
import org.springframework.data.repository.CrudRepository;

public interface RouterRepository extends CrudRepository<Router, Integer> {

    @Override
    <S extends Router> S save(S s);

    @Override
    <S extends Router> Iterable<S> saveAll(Iterable<S> iterable);
}
