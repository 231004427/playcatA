package com.sunlin.playcat.domain;
/**
 * Created by sunlin on 2017/8/11.
 */
public class Area {
    private int id;
    private String name;
    private int parent_id;
    private int type;
    private String sort_name;
    private int is_oversea;
    private String area_code;
    private int status;

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getIs_oversea() {
        return is_oversea;
    }

    public int getParent_id() {
        return parent_id;
    }

    public int getStatus() {
        return status;
    }

    public String getArea_code() {
        return area_code;
    }

    public String getName() {
        return name;
    }

    public String getSort_name() {
        return sort_name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIs_oversea(int is_oversea) {
        this.is_oversea = is_oversea;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public void setSort_name(String sort_name) {
        this.sort_name = sort_name;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
