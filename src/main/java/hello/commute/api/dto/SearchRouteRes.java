package hello.commute.api.dto;

import hello.commute.api.dto.Path;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchRouteRes {

    private int searchType;
    private int outTrafficCheck;
    private int busCount;
    private int subwayCount;
    private int subwayBusCount;
    private double pointDistance;
    private int startRadius;
    private int endRadius;
    private List<Path> pathList;
    private JSONObject path;

    public SearchRouteRes(JSONObject jsonResult) {
        this.searchType = (int) jsonResult.get("searchType");
        this.outTrafficCheck = (int) jsonResult.get("outTrafficCheck");
        this.busCount = (int) jsonResult.get("busCount");
        this.subwayBusCount = (int) jsonResult.get("subwayBusCount");
        this.subwayCount = (int) jsonResult.get("subwayCount");
        this.pointDistance = (double) jsonResult.get("pointDistance");
        this.startRadius = (int) jsonResult.get("startRadius");
        this.endRadius = (int) jsonResult.get("endRadius");
        JSONArray path = jsonResult.getJSONArray("path");
        for (int i=0; i<path.length(); i++){
            JSONObject eachPath= (JSONObject) path.get(i);
            pathList.add(new Path(eachPath));
        }
    }
}
