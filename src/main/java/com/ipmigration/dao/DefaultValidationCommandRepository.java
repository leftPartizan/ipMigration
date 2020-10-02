package com.ipmigration.dao;


import com.ipmigration.model.DefaultValidationCommand;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DefaultValidationCommandRepository extends CrudRepository<DefaultValidationCommand, Integer> {

    @Override
    List<DefaultValidationCommand> findAll();

    List<DefaultValidationCommand> findAllByOrderById();
}
