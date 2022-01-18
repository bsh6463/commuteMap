package hello.commute.api;

import java.util.List;

public class Path {

    private int pathType;
    private double trafficDistance;
    private int totalWalk;
    private int totalTime;
    private int payment;
    private int busTransitCount;
    private int subwayTransitCount;
    private String mapOj;
    private String firstStartStation;
    private String lastEndStation;
    private int totalStationCount;
    private int busStationCount;
    private int subwayStationCount;
    private double totalDistance;
    private List<SubPath> subPathList;
}
