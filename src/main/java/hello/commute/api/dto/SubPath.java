package hello.commute.api.dto;

import hello.commute.api.OdSayClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@Getter
@RequiredArgsConstructor
public class SubPath {

    private OdSayClient odSayClient;

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
    private String stationId;

    private String way;
    private int wayCode;
    private String door;

    private int startID;
    private int endID;
    private JSONObject passStopList;
    private ArrayList<Station> stations;

    //실시간 도착정보
    private int arrivalMin;
    private int leftStation;


    @Autowired
    public SubPath(JSONObject eachSubPath, int index, OdSayClient odSayClient) {
        this.trafficType = (int) eachSubPath.get("trafficType");
        this.distance = (int) eachSubPath.get("distance");  //왜 double로 안들어옴?
        this.sectionTime = (int) eachSubPath.get("sectionTime");
        this.odSayClient= odSayClient;

        if (trafficType != 3){
            this.stationCount = (int) eachSubPath.get("stationCount");
            this.laneJasonArray = eachSubPath.getJSONArray("lane");
            this.laneJson= (JSONObject) laneJasonArray.get(0);
            this.lane= new Lane(laneJson, trafficType);
            this.startName = (String) eachSubPath.get("startName");
            this.startX = (double) eachSubPath.get("startX");
            this.startY = (double) eachSubPath.get("startY");

            this.endName = (String) eachSubPath.get("endName");
            this.endX = (double) eachSubPath.get("endX");
            this.endY = (double) eachSubPath.get("endY");

            if (trafficType == 1){
                //지하철의 경우에만
                this.way = (String) eachSubPath.get("way");
                if (index==0){
                    //지하철 첫 번 째 경로에만.
                    this.wayCode = (int) eachSubPath.get("wayCode");
                }
                this.door = (String) eachSubPath.get("door");
            }else {
                //버스의 경우
                this.stationId = odSayClient.getStationId(startName, String.valueOf(startX), String.valueOf(startY));
                SearchRealTimeStationReq searchRealTimeStationReq = new SearchRealTimeStationReq(stationId, this.lane.getBusID());
                SearchRealTimeStationRes realTimeBusInfo = odSayClient.getRealTimeBusStation(searchRealTimeStationReq);
                this.arrivalMin = realTimeBusInfo.getArrivalMin();
                this.leftStation = realTimeBusInfo.getLeftStation();
            }

            this.startID = (int) eachSubPath.get("startID");
            this.endID = (int) eachSubPath.get("endID");

            this.passStopList = eachSubPath.getJSONObject("passStopList");
            JSONArray stationList = passStopList.getJSONArray("stations");
            stations = new ArrayList<>();
            for (int i=0; i<stationList.length(); i++){
                JSONObject stationJson = (JSONObject) stationList.get(i);
                stations.add(new Station(stationJson, trafficType));
            }

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


        public Lane(JSONObject laneJson, int trafficType) {
            if (trafficType == 1){
                //지하철
                this.name = (String) laneJson.get("name");
                this.subwayCode = (int) laneJson.get("subwayCode");
                this.subwayCityCode = (int) laneJson.get("subwayCityCode");
            }else if (trafficType == 2){
                //버스
                this.busNo = (String) laneJson.get("busNo");
                this.type = (int) laneJson.get("type");
                this.busID = (int) laneJson.get("busID");
            }


        }
    }

    @Getter
    public static class Station{

        private int index;
        private String stationName;
        private String x;
        private String y;
        private String isNonStop;

        public Station(JSONObject stationJson, int trafficType) {
            this.index = (int) stationJson.get("index");
            this.stationName = (String) stationJson.get("stationName");
            this.x = (String) stationJson.get("x");
            this.y = (String) stationJson.get("y");

            if (trafficType == 2){
                this.isNonStop = (String) stationJson.get("isNonStop");
            }

        }
    }
}
