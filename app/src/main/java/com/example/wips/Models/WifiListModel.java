package com.example.wips.Models;

public class WifiListModel {
    private String wifi_name;
    private String rss;
    private boolean isSelected = false;

    public WifiListModel(String wifi_name, String rss) {
        this.wifi_name = wifi_name;
        this.rss = rss;
    }
    public String getWifi_name() {
        return wifi_name;
    }
    public String getRss() {
        return rss;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    public boolean isSelected() {
        return isSelected;
    }
}
