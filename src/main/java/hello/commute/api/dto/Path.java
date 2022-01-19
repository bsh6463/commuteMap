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
public class Path {

    private int pathType;

    private JSONObject info;
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
    private int totalWalkTime;

    private List<SubPath> subPathList;
    private JSONObject subPath;

    public Path(JSONObject path) {
        this.info = (JSONObject) path.get("info");

        this.pathType = (int) info.get("pathType");
        this.trafficDistance = (double) info.get("trafficDistance");
        this.totalWalk = (int) info.get("totalWalk");
        this.totalTime = (int) info.get("totalTime");
        this.payment = (int) info.get("payment");
        this.busTransitCount = (int) info.get("busTransitCount");
        this.subwayTransitCount = (int) info.get("subwayTransitCount");
        this.mapOj = (String) info.get("mapOj");
        this.firstStartStation = (String) info.get("firstStartStation");
        this.lastEndStation = (String) info.get("lastEndStation");
        this.totalStationCount = (int) info.get("totalStationCount");
        this.busStationCount = (int) info.get("busStationCount");
        this.subwayStationCount = (int) info.get("subwayStationCount");
        this.totalDistance = (double) info.get("totalDistance");
        this.totalWalk = (int) info.get("totalWalk");

        JSONArray subPath = path.getJSONArray("subPath");
        for (int i=0; i<subPath.length(); i++){
            JSONObject eachSubPath = (JSONObject) subPath.get(i);
            subPathList.add(new SubPath(eachSubPath));
        }


    }
}
