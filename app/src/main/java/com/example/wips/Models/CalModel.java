package com.example.wips.Models;

public class CalModel {
    String name;
    String version;
    int image;

    public CalModel(String name, String version, int image) {
        this.name = name;
        this.version = version;
        this.image=image;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public int getImage() {
        return image;
    }

}
