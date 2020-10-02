package com.ipmigration.dao;

import com.ipmigration.model.CommandLines;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommandLinesRepository extends CrudRepository<CommandLines, Integer> {

    @Override
    long count();

    @Override
    List<CommandLines> findAll();

    List<CommandLines> findAllByOrderById();

    @Override
    void deleteAll();

    @Override
    Optional<CommandLines> findById(Integer integer);

    @Override
    <S extends CommandLines> List<S> saveAll(Iterable<S> iterable);

}
