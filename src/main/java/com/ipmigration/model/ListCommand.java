package com.ipmigration.model;

import java.util.List;

public class ListCommand {

    List<CommandLines> list;

    public ListCommand() {
    }

    public ListCommand(List<CommandLines> list) {
        this.list = list;
    }

    public List<CommandLines> getList() {
        return list;
    }

    public void setList(List<CommandLines> list) {
        this.list = list;
    }
}
