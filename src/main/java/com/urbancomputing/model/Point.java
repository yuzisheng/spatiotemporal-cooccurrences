package com.urbancomputing.model;

/**
 * Point
 *
 * @author yuzisheng
 * @date 2021/11/24
 */
public class Point {
    /**
     * point id
     */
    int pid;
    /**
     * longitude or x
     */
    double lng;
    /**
     * latitude or y
     */
    double lat;

    public Point(int pid, double lng, double lat) {
        this.pid = pid;
        this.lng = lng;
        this.lat = lat;
    }

    public int getPid() {
        return pid;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }

    public String toWKT() {
        return "POINT (" + lng + " " + lat + ")";
    }
}
