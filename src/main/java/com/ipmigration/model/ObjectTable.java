package com.ipmigration.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "object")
public class ObjectTable {

    @Id
    int id;
    int object_type_id;
    String name;
    Integer parent_id;

    public ObjectTable() {
    }

    public ObjectTable(int id, int object_type_id, String name, Integer parent_id) {
        this.id = id;
        this.object_type_id = object_type_id;
        this.name = name;
        this.parent_id = parent_id;
    }

    @Override
    public String toString() {
        return "ObjectTable{" +
                "id=" + id +
                ", object_type_id=" + object_type_id +
                ", name='" + name + '\'' +
                ", parent_id=" + parent_id +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getObject_type_id() {
        return object_type_id;
    }

    public void setObject_type_id(int object_type_id) {
        this.object_type_id = object_type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }
}
