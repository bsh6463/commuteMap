package hello.commute.api.dto;

import hello.commute.api.exception.EndOfServiceException;
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

        JSONObject arrivalInfos = jsonResult.getJSONObject("result").getJSONArray("real").getJSONObject(0);
        if (!arrivalInfos.isNull("arrival1")){
            JSONObject arrivalInfo = arrivalInfos.getJSONObject("arrival1");
            this.arrivalSec = (int) arrivalInfo.get("arrivalSec");
            this.leftStation = (int) arrivalInfo.get("leftStation");
            this.arrivalMin = arrivalSec/60;
        }else {
            throw new EndOfServiceException("해당 정류장의 버스 운행이 종료되었습니다.");
        }



    }
}
