package com.example.wips.Models;

public class Building{

    String building_name ;
    String buildin_id ;
    String place_id ;

    public Building(String place_id, String building_name, String buildin_id) {
        this.place_id = place_id ;
        this.building_name = building_name;
        this.buildin_id = buildin_id;
    }

    public String getPlace_id() { return place_id; }
    public String getBuilding_name() {
        return building_name;
    }
    public String getBuildin_id() {
        return buildin_id;
    }
}
