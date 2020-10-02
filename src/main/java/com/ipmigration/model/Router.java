package com.ipmigration.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sdb_routers")
public class Router {

    @Id
    private int id;
    private String name;
    private int is_valid = 1;
    private Integer new_id_ip = null;
    private Integer new_id = null;

    public Router() {
    }

    public Router(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Router{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", is_valid=" + is_valid +
                ", new_id_ip=" + new_id_ip +
                ", new_id=" + new_id +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(int is_valid) {
        this.is_valid = is_valid;
    }
}
