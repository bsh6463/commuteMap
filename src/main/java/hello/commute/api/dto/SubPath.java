package hello.commute.api.dto;

import hello.commute.api.client.OdSayClient;
import hello.commute.api.client.SeoulClient;
import hello.commute.api.exception.EndOfServiceException;
import hello.commute.api.exception.OutOfServiceException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class SubPath {

    //필수값 trafficType = 3 도보
    private int trafficType;
    private int distance;    //왜 double로 안들어옴?    
    private int sectionTime;

    //버스, 지하철 타는 경우
    //trafficType = 1 -> 지하철
    //trafficType = 2 -> 버스
    private int stationCount;

    @Setter
    private JSONArray laneJasonArray;
    @Setter
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

    //호선 매핑
    private Map<Integer, String> seoulSubwayIdMap;
    private Map<Integer, String> updnLineMap;
    private Map<Integer, String> updnLine2ndMap;

    //지하철 도착 정보
    private String arrivalMessage;
    private String updnLine;

    public SubPath(JSONObject eachSubPath, int index) {
        init();
        this.trafficType = (int) eachSubPath.get("trafficType");
        this.distance = (int) eachSubPath.get("distance");  //왜 double로 안들어옴?
        this.sectionTime = (int) eachSubPath.get("sectionTime");

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
                //지하철의 경우에만, 방면정보
                this.way = (String) eachSubPath.get("way");
                //if (index==0){
                    //지하철 첫 번 째 경로에만.waycode : 1(상행), 2(하행)
                this.wayCode = (int) eachSubPath.get("wayCode");

               // }
                this.door = (String) eachSubPath.get("door");
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


    @Autowired
    public void getStationIdAndRealTimeInfo(OdSayClient odSayClient, SeoulClient seoulClient) {

        if (this.trafficType == 2){
            this.stationId = odSayClient.getStationId(startName, String.valueOf(startX), String.valueOf(startY));
            SearchRealTimeStationReq searchRealTimeStationReq = new SearchRealTimeStationReq(stationId, this.lane.getBusID());
            SearchRealTimeStationRes realTimeBusInfo = odSayClient.getRealTimeBusStation(searchRealTimeStationReq);
            this.arrivalMin = realTimeBusInfo.getArrivalMin();
            this.leftStation = realTimeBusInfo.getLeftStation();
        }else if(this.trafficType == 1){
            int odsaySubwayCode = this.lane.subwayCode;

            if (seoulSubwayIdMap.containsKey(odsaySubwayCode)){
                //노선 id odsaty -> seoul
                String seoulSubwayId = seoulSubwayIdMap.get(odsaySubwayCode);

                //진행방향 : 상행, 하행, 외선, 내선 확인
                if (seoulSubwayId.equals("1002")){
                    updnLine = updnLine2ndMap.get(wayCode);
                }else {
                    updnLine = updnLineMap.get(wayCode);
                }

                try{
                    SeoulSubwayArrivalInfoRes arrivalInfoRes = seoulClient.getRealtimeInfo(startName, seoulSubwayId, updnLine);
                    this.arrivalMessage = arrivalInfoRes.getArrivalMessage();
                }catch (NoResultException | OutOfServiceException | EndOfServiceException e){
                    arrivalMessage = e.getMessage();
                }
            }
        }
    }


    private void init(){
        seoulSubwayIdMap = new LinkedHashMap<>();
        seoulSubwayIdMap.put(1, "1001");
        seoulSubwayIdMap.put(2,"1002");
        seoulSubwayIdMap.put(3,"1003");
        seoulSubwayIdMap.put(4,"1004");
        seoulSubwayIdMap.put(5,"1005");
        seoulSubwayIdMap.put(6,"1006");
        seoulSubwayIdMap.put(7,"1007");
        seoulSubwayIdMap.put(8,"1008");
        seoulSubwayIdMap.put(9,"1009");
        seoulSubwayIdMap.put(101,"1065");
        seoulSubwayIdMap.put(104,"1063");
        seoulSubwayIdMap.put(108,"1067");
        seoulSubwayIdMap.put(109,"1077");
        updnLineMap =new LinkedHashMap<>();
        updnLineMap.put(1, "상행");
        updnLineMap.put(2, "하행");
        updnLine2ndMap = new LinkedHashMap<>();
        updnLine2ndMap.put(1, "외선");
        updnLine2ndMap.put(2, "내선");
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
