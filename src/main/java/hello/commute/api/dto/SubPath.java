package hello.commute.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubPath {

    //필수값 trafficType = 3 도보
    private int trafficType;
    private int distance;    //왜 double로 안들어옴?    
    private int sectionTime;

    //버스, 지하철 타는 경우
    //trafficType = 1 -> 지하철
    //trafficType = 2 -> 버스
    private int stationCount;
    private JSONArray laneJasonArray;
    private JSONObject laneJson;
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
        this.trafficType = (int) eachSubPath.get("trafficType");
        this.distance = (int) eachSubPath.get("distance");  //왜 double로 안들어옴?
        this.sectionTime = (int) eachSubPath.get("sectionTime");

        if (trafficType != 3){
            this.stationCount = (int) eachSubPath.get("stationCount");
            this.laneJasonArray = eachSubPath.getJSONArray("lane");
            this.laneJson= (JSONObject) laneJasonArray.get(0);
            this.lane= new Lane(laneJson);
            this.startName = (String) eachSubPath.get("startName");
            this.startX = (double) eachSubPath.get("startX");
            this.startY = (double) eachSubPath.get("startY");
            this.endName = (String) eachSubPath.get("endName");
            this.endX = (double) eachSubPath.get("endX");
            this.endY = (double) eachSubPath.get("endY");
            this.way = (String) eachSubPath.get("way");
            this.wayCode = (int) eachSubPath.get("wayCode");
            this.door = (String) eachSubPath.get("door");
            this.startID = (int) eachSubPath.get("startID");
            this.endID = (int) eachSubPath.get("endID");
        }
    }

    @Getter
    public static class Lane{
        private String name;
        private String busNo;
        private int type;
        private int busID;
        private int subwayCode;
        private int subwayCityCode;
        private JSONObject passStopList;
        private List<Station> stations;

        public Lane(JSONObject landJson) {
            this.name = (String) landJson.get("name");
            this.busNo = (String) landJson.get("busNo");
            this.type = (int) landJson.get("type");
            this.busID = (int) landJson.get("busID");
            this.subwayCode = (int) landJson.get("subwayCode");
            this.subwayCityCode = (int) landJson.get("subwayCityCode");
            this.passStopList = landJson.getJSONObject("passStopList");
            JSONArray stationList = passStopList.getJSONArray("stations");
            for (int i=0; i<stationList.length(); i++){
                JSONObject stationJson = (JSONObject) stationList.get(i);
                stations.add(new Station(stationJson));
            }

        }
    }

    public static class Station{

        private String stationName;
        private String x;
        private String y;
        private String isNonStop;

        public Station(JSONObject stationJson) {
            this.stationName = (String) stationJson.get("stationName");
            this.x = (String) stationJson.get("x");
            this.y = (String) stationJson.get("y");
            this.isNonStop = (String) stationJson.get("isNonStop");

        }
    }
}
