package com.ipmigration.dao;

import com.ipmigration.model.ObjectTable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ObjectRepository extends CrudRepository<ObjectTable, Integer> {

    @Override
    Optional<ObjectTable> findById(Integer integer);

    // получить все записи айпи адресов и диапозонов, которые принадлежат определённому обьекту
    @Query(nativeQuery = true, value =
            "select * from OBJECT where object_type_id in (select id from OBJECT_TYPE where NAME='Ip Address' or NAME='Ip Range') and PARENT_ID = ?1")
    List<ObjectTable> findObjectToParent_id(Integer parent_id);

    // получить все записи айпи адресов и диапозонов, которые ни кому не принадлежат
    @Query(nativeQuery = true, value =
            "select * from OBJECT where object_type_id in (select id from OBJECT_TYPE where NAME='Ip Address' or NAME='Ip Range') and PARENT_ID is null")
    List<ObjectTable> findObjectToParent_id();

    @Override
    List<ObjectTable> findAll();

    // получить список роутеров, ссылающийся на айпи роутер
    @Query(nativeQuery = true, value = "select * from OBJECT where id in (select OBJECT_ID from REFERENCE where refer=?1)")
    List<ObjectTable> findAllByReference(int index);
}
