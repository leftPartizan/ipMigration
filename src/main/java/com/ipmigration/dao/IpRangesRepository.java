package com.ipmigration.dao;

import com.ipmigration.model.IpRanges;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IpRangesRepository extends CrudRepository<IpRanges, Integer> {

    @Override
    <S extends IpRanges> S save(S s);

    @Override
    <S extends IpRanges> Iterable<S> saveAll(Iterable<S> iterable);

}
