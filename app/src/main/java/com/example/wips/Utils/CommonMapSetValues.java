package com.example.wips.Utils;

import java.util.*;

import kotlin.jvm.internal.markers.KMutableMap;

public class CommonMapSetValues {

    /*public static void main(String[] args) {
        CommonMapSetValues obj = new CommonMapSetValues();
        obj.addKeyValue("abc", new String[] {"ax1","au2","au3"});
        obj.addKeyValue("def", new String[] {"ax1","au6"});
        obj.addKeyValue("ijk", new String[] {"ax1","au2"});
        System.out.println(obj.toString());
        System.out.println(obj.getFrequencyMap());
        System.out.println(obj.getFrequencyMap().getCommon(2));
        System.out.println(obj.getFrequencyMap().getCommon(3));
        System.out.println(obj.getFrequencyMap().getCommon(4));
    }*/

    private Map<String, Set<String>> map;
    private Map<String, String> Hashmap;

    public CommonMapSetValues(HashMap<String,String> Hashmap) {
        this.Hashmap = Hashmap;
    }
    public ArrayList<String> countFrequencies()
    {
        // hashmap to store the frequency of element
        Map<String, Integer> hm = new HashMap<String, Integer>();
        Map<String,String> list = this.Hashmap;

        for (Map.Entry<String, String> i : list.entrySet()) {
            Integer j = hm.get(i.getValue());
            hm.put(i.getValue(), (j == null) ? 1 : j + 1);
        }
        if(hm.isEmpty()){
            ArrayList<String> error = new ArrayList<String>();
            error.add("LNC");
            return error;
        }
        int maxValueInMap=(Collections.max(hm.values()));
        ArrayList<String> rooms = new ArrayList<String>();
        for (Map.Entry<String, Integer> i : hm.entrySet()) {  // Iterate through HashMap
            if (i.getValue()==maxValueInMap) {
                rooms.add(i.getKey());
            }
        }

        // displaying the occurrence of elements in the arraylist
       return rooms;
    }

    public void addKeyValue(String key, String[] valueArray) {
        Set<String> valueSet;
        if(map.containsKey(key)) {
            valueSet = map.get(key);
        } else {
            valueSet = new HashSet<String>();
            map.put(key, valueSet);
        }
        Collections.addAll(valueSet, valueArray);
    }

  public FrequencyMap getFrequencyMap() {
        Map<String, Integer> frequencies = new HashMap<String, Integer>();
        for(String key : this.map.keySet()) {
            for(String element : this.map.get(key)) {
                int count;
                if(frequencies.containsKey(element)) {
                    count = frequencies.get(element);
                } else {
                    count = 1;
                }
                frequencies.put(element, count + 1);
            }
        }
        return new FrequencyMap(frequencies);
    }

    @Override public String toString() {
        return map.toString();
    }
}

 class FrequencyMap implements Comparator<String> {

    Map<String, Integer> map;
    public FrequencyMap(Map<String, Integer> map) {
        this.map = map;
    }

    public int compare(String a, String b) {
        if (map.get(a) >= map.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }

    public ArrayList<String> getCommon(int threshold) {
        ArrayList<String> common = new ArrayList<String>();
        for(String key : this.map.keySet()) {
            if(this.map.get(key) >= threshold) {
                common.add(key);
            }
        }
        return common;
    }

    @Override public String toString() {
        return this.map.toString();
    }
}