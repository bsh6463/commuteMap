package hello.commute.api.client;

import hello.commute.api.dto.SeoulSubwayArrivalInfoRes;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SeoulClientTest {

    @Autowired
    SeoulClient seoulClient;

    @Test
    void request(){
        SeoulSubwayArrivalInfoRes realtimeInfo = seoulClient.getRealtimeInfo("잠실", "1002", "상행");
        System.out.println(realtimeInfo);

    }
}