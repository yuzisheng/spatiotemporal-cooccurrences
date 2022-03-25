package com.urbancomputing.model;

/**
 * ST Point
 *
 * @author yuzisheng
 * @date 2021/11/24
 */
public class STPoint extends Point {
    /**
     * 10 digit timestamp in seconds
     */
    long time;

    public STPoint(int pid, double lng, double lat, long time) {
        super(pid, lng, lat);
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}
