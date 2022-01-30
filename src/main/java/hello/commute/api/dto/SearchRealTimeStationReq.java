package hello.commute.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchRealTimeStationReq {

    private String arsId;
    private int busNumber;

}
