package com.ipmigration.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Default_Validation_Command")
public class DefaultValidationCommand {

    @Id
    int id;
    String command;
    String type;

    public DefaultValidationCommand() {
    }

    public DefaultValidationCommand(int id, String command, String type) {
        this.id = id;
        this.command = command;
        this.type = type;
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
