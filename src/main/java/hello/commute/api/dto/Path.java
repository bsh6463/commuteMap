package hello.commute.api.dto;

import hello.commute.api.client.OdSayClient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class Path {

    private int pathType;

    @Setter
    private JSONObject info;
    private double trafficDistance;
    private int totalWalk;
    private int totalTime;
    private int payment;
    private int busTransitCount;
    private int subwayTransitCount;
    private String mapObj;
    private String firstStartStation;
    private String lastEndStation;
    private int totalStationCount;
    private int busStationCount;
    private int subwayStationCount;
    private double totalDistance;
    private int totalWalkTime;

    private ArrayList<SubPath> subPathList;
    private JSONObject subPath;

    public Path(JSONObject path, OdSayClient odSayClient) {
        this.pathType = (int) path.get("pathType");

        this.info = (JSONObject) path.get("info");
        this.trafficDistance = (double) info.get("trafficDistance");
        this.totalWalk = (int) info.get("totalWalk");
        this.totalTime = (int) info.get("totalTime");
        this.payment = (int) info.get("payment");
        this.busTransitCount = (int) info.get("busTransitCount");
        this.subwayTransitCount = (int) info.get("subwayTransitCount");
        this.mapObj = (String) info.get("mapObj");
        this.firstStartStation = (String) info.get("firstStartStation");
        this.lastEndStation = (String) info.get("lastEndStation");
        this.totalStationCount = (int) info.get("totalStationCount");
        this.busStationCount = (int) info.get("busStationCount");
        this.subwayStationCount = (int) info.get("subwayStationCount");
        this.totalDistance = (double) info.get("totalDistance");
        this.totalWalk = (int) info.get("totalWalk");

        JSONArray subPath = path.getJSONArray("subPath");
        subPathList = new ArrayList<>();
        for (int i=0; i<subPath.length(); i++){
            JSONObject eachSubPath = (JSONObject) subPath.get(i);
            subPathList.add(new SubPath(eachSubPath, i));
        }


    }
}
