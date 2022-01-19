package hello.commute.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubPath {

    //필수값 trafficType = 3
    private int trafficType;
    private double distance;
    private int sectionTime;

    //버스, 지하철 타는 경우
    //trafficType = 3
    private int stationCount;
    private JSONObject laneJason;
    private Lane lane;
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

    public SubPath(JSONObject eachSubPath) {
    }

    public static class Lane{
        private String name;
        private String busNo;
        private int type;
        private int busID;
        private int subwayCode;
        private int subwayCityCode;
        private PassStop passStopList;
    }

    public static class PassStop{
       List<Station> stations;

       public static class Station{
           private String stationName;
           private String x;
           private String y;
           private String isNonStop;
       }
    }
}
