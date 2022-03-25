package com.urbancomputing.cooccurrence;

import com.urbancomputing.model.CheckIn;
import com.urbancomputing.model.Point;
import com.urbancomputing.model.STPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * data preprocess
 *
 * @author yuzisheng
 * @date 2021/11/24
 */
public class DataPreProcess {
    /**
     * get check-in data according to user report points and locations with spatio-temporal bound
     *
     * @param userPoints            user report points including user id, lng, lat, and time
     * @param locations             locations of poi including location id, lng, and lat
     * @param spatialBoundInMeter   spatial bound in meter to determine whether the two are close enough in space
     * @param temporalBoundInSecond temporal bound in second to determine whether the two are close enough in time
     * @return list of check-in data
     */
    public static List<CheckIn> getCheckInData(List<STPoint> userPoints,
                                               List<Point> locations,
                                               double spatialBoundInMeter,
                                               int temporalBoundInSecond) {
        ArrayList<CheckIn> checkIns = new ArrayList<>();
        for (STPoint userPoint : userPoints) {
            for (Point location : locations) {
                if (distance(userPoint, location) < spatialBoundInMeter) {
                    checkIns.add(new CheckIn(
                            userPoint.getPid(), location.getPid(), userPoint.getTime() / temporalBoundInSecond));
                }
            }
        }
        return checkIns;
    }

    public static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.getLng() - p2.getLng(), 2) + Math.pow(p1.getLat() - p2.getLat(), 2));
    }
}
