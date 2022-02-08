package hello.commute.api.client;

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
        //JSONObject jsonResult = seoulClient.getRealtimeInfo("잠실");
        //System.out.println(jsonResult);

    }
}