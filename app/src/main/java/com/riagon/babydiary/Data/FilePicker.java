package com.riagon.babydiary.Data;

import java.io.Serializable;

public class FilePicker implements Serializable {
    private String name;
    private String datetime;
    private String size;
    private String path;

    public FilePicker(String name, String datetime, String size, String path) {
        this.name = name;
        this.datetime = datetime;
        this.size = size;
        this.path = path;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
