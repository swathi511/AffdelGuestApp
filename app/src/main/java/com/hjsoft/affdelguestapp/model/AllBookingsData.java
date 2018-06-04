package com.hjsoft.affdelguestapp.model;

import java.io.Serializable;

/**
 * Created by hjsoft on 19/5/18.
 */
public class AllBookingsData implements Serializable {

    String route,date,status;

    public AllBookingsData(String route, String date, String status)
    {
        this.route=route;
        this.date=date;
        this.status=status;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
