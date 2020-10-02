package com.ipmigration.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COMMAND_LINES")
public class CommandLines {

    @Id
    int id;
    String command;
    String type;

    public CommandLines() {
    }

    public CommandLines(int id, String command, String type) {
        this.id = id;
        this.command = command;
        this.type = type;
    }

    @Override
    public String toString() {
        return "CommandLines{" +
                "id=" + id +
                ", command='" + command + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
