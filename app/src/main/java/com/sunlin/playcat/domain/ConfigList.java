package com.sunlin.playcat.domain;

import java.util.List;

/**
 * Created by sunlin on 2017/9/13.
 */

public class ConfigList {
    int type;
    String name;
    List<Config> list;

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public List<Config> getList() {
        return list;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setList(List<Config> list) {
        this.list = list;
    }
}
