package hello.commute.api.dto;

import lombok.Data;

@Data
public class SeoulSubwayArrivalInfoRes {
private String arrivalMessage;
private String way;
private String line;

    public SeoulSubwayArrivalInfoRes(String arrivalMessage, String way) {
        this.arrivalMessage =arrivalMessage;
        this.way = way;
    }
}
