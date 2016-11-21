package net.nortlam.regex;

import java.io.Serializable;

public class Country implements Serializable {

    private String name;
    private String nameOfficially;
    private String url;
    private String flag;
    
    public Country() {
    }

    public Country(String name, String nameOfficially, String url, String flag) {
        this.name = name;
        this.nameOfficially = nameOfficially;
        this.url = url;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    public String getNameOfficially() {
        return nameOfficially;
    }

    public void setNameOfficially(String nameOfficially) {
        this.nameOfficially = nameOfficially != null ? nameOfficially.trim() : null;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url != null ? url.trim() : null;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag != null ? flag.trim() : null;
    }
    
    @Override
    public String toString() {
        return String.format("COUNTRY: %s, %s, %s, %s", name, nameOfficially, url, flag);
    }
    
}
