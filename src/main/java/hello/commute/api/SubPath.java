package hello.commute.api;

import java.util.List;

public class SubPath {

    private int trafficType;
    private double distance;
    private int sectionTime;
    private int stationCount;
    private List<Lane> laneList;
    private String startName;
    private double startX;
    private double startY;
    private String endName;
    private double endX;
    private double endY;
    private String way;
    private int wayCode;
    private String door;
    private int startID;
    private int endID;


    public static class Lane{
        private String name;
        private String busNo;
        private int type;
        private int busID;
        private int subwayCode;
        private int subwayCityCode;
        private List<PassStop> passStopList;
    }

    public static class PassStop{
        private String stationName;
        private String x;
        private String y;
        private String isNonStop;
    }
}
