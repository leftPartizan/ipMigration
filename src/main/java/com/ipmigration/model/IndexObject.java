package com.ipmigration.model;

// используется как входной параметр для метода в контроллере
public class IndexObject {
    private Integer id;
    private String type;

    public IndexObject() {
    }

    @Override
    public String toString() {
        return "IndexObject{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
