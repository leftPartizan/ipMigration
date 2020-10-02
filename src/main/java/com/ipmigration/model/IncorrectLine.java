package com.ipmigration.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SDB_incorrect_line")
public class IncorrectLine {

    @Id
    private int number_line_to_file;
    private String line;


    public IncorrectLine() {
    }

    public IncorrectLine(int number_line_to_file, String line) {
        this.number_line_to_file = number_line_to_file;
        this.line = line;
    }

    public int getNumber_line_to_file() {
        return number_line_to_file;
    }

    public void setNumber_line_to_file(int number_line_to_file) {
        this.number_line_to_file = number_line_to_file;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "IncorrectLine{" +
                "number_line_to_file=" + number_line_to_file +
                ", line='" + line + '\'' +
                '}';
    }
}
