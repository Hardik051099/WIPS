package com.example.wips.Models;

public class WifiListModel {
    private String wifi_name;
    private String bssid;
    private String rss;
    private boolean isSelected = false;

    public WifiListModel(String wifi_name,String bssid, String rss) {
        this.wifi_name = wifi_name;
        this.bssid = bssid;
        this.rss = rss;
    }
    public String getWifi_name() {
        return wifi_name;
    }
    public String getRss() {
        return rss;
    }
    public String getBssid(){
        return bssid;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public boolean isSelected() {
        return isSelected;
    }
}
