package com.urbancomputing.model;

/**
 * check-in data
 *
 * @author yuzisheng
 * @date 2021/11/22
 */
public class CheckIn {
    /**
     * user id
     */
    int uid;
    /**
     * location id
     */
    int lid;
    /**
     * 10 digit timestamp is used to represent check-in time in seconds
     */
    long time;

    public CheckIn(int uid, int lid, long time) {
        this.uid = uid;
        this.lid = lid;
        this.time = time;
    }

    public int getUid() {
        return uid;
    }

    public int getLid() {
        return lid;
    }

    public long getTime() {
        return time;
    }
}
