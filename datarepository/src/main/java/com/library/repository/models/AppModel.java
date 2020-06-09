package com.library.repository.models;

public class AppModel extends BaseM{
    private String appname;
    private int id;

    public AppModel(String appname, int id) {
        this.appname = appname;
        this.id = id;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
