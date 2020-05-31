package com.baronqi.jetpackvideo.model;

public class Destination {

    private String path;
    private boolean isDefault;
    private boolean isActivity;
    private int id;
    private boolean login;
    private String clazzName;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isActivity() {
        return isActivity;
    }

    public void setIsActivity(boolean isActivity) {
        this.isActivity = isActivity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

}
