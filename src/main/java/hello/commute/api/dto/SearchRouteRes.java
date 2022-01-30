package hello.commute.api.dto;

import hello.commute.api.OdSayClient;
import hello.commute.api.dto.Path;
import lombok.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class SearchRouteRes {

    private OdSayClient odSayClient;
    private JSONObject result;
    private int searchType;
    private int outTrafficCheck;
    private int busCount;
    private int subwayCount;
    private int subwayBusCount;
    private int pointDistance; //왜 double로안들어옴?
    private int startRadius;
    private int endRadius;
    private ArrayList<Path> pathList;
    private JSONObject path;

    @Autowired
    public SearchRouteRes(JSONObject jsonResult, OdSayClient odSayClient) {
        this.result = jsonResult.getJSONObject("result");
        this.searchType = (int) result.get("searchType");
        this.outTrafficCheck = (int) result.get("outTrafficCheck");
        this.busCount = (int) result.get("busCount");
        this.subwayBusCount = (int) result.get("subwayBusCount");
        this.subwayCount = (int) result.get("subwayCount");
        this.pointDistance = (int) result.get("pointDistance");
        this.startRadius = (int) result.get("startRadius");
        this.endRadius = (int) result.get("endRadius");
        JSONArray path = result.getJSONArray("path");
        pathList = new ArrayList<>();
        for (int i=0; i<path.length(); i++){
            JSONObject eachPath= (JSONObject) path.get(i);
            pathList.add(new Path(eachPath, odSayClient));
        }
    }
}
