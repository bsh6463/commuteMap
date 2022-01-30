package hello.commute.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;

@Data
@AllArgsConstructor
public class SearchRealTimeStationRes {

    private int arrivalSec;
    private int arrivalMin;
    private int leftStation;

    public SearchRealTimeStationRes(JSONObject jsonResult) {

        JSONObject arrivalInfo = jsonResult.getJSONObject("result").getJSONArray("real").getJSONObject(0).getJSONObject("arrival1");
        this.arrivalSec = (int) arrivalInfo.get("arrivalSec");
        this.leftStation = (int) arrivalInfo.get("leftStation");
        this.arrivalMin = arrivalSec/60;

    }
}
