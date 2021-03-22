package com.example.wips.Models;

public class Map {
    String place_name ;
    String place_id ;

    public Map(String place_name, String place_id) {
        this.place_name = place_name;
        this.place_id = place_id;
    }

    public String getPlace_name() {
        return place_name;
    }
    public String getPlace_id() {
        return place_id;
    }

}
