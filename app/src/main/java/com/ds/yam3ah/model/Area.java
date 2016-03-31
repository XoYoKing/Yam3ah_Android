package com.ds.yam3ah.model;

import java.util.ArrayList;

/**
 * Created by Shivangi on 6/4/2015.
 */
public class Area {

    private String state;
    private ArrayList<City> cityarr;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ArrayList<City> getCityarr() {
        return cityarr;
    }

    public void setCityarr(ArrayList<City> cityarr) {
        this.cityarr = cityarr;
    }
}
