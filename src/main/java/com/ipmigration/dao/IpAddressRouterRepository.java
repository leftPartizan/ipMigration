package com.ipmigration.dao;

import com.ipmigration.model.IpAddressRouter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface IpAddressRouterRepository extends CrudRepository<IpAddressRouter, Integer> {

    @Override
    <S extends IpAddressRouter> S save(S s);

    @Override
    <S extends IpAddressRouter> Iterable<S> saveAll(Iterable<S> iterable);

}
