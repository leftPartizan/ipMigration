package com.ipmigration.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "validation")
public class Validation {


    @Id
    int id;
    String table_name;
    String incorrect_value;
    String text;

    public Validation() {
    }

    public Validation(int id, String table_name, String incorrect_value, String text) {
        this.id = id;
        this.table_name = table_name;
        this.incorrect_value = incorrect_value;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getIncorrect_value() {
        return incorrect_value;
    }

    public void setIncorrect_value(String incorrect_value) {
        this.incorrect_value = incorrect_value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Validation{" +
                "id=" + id +
                ", table_name='" + table_name + '\'' +
                ", incorrect_value='" + incorrect_value + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
