package hello.commute.api.client;


import hello.commute.api.dto.SearchRouteReq;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class OdSayClientTest {
    @Autowired OdSayClient odSayClient;
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("send wrong key: getStationId")
    void wrongKey_getStationId(){
        odSayClient.changeKey("wrongKey");
        Assertions.assertThrows(IllegalStateException.class, () ->{
            odSayClient.getStationId("서울역", "126.9707979959352","37.5547020732267");
        });
    }

    @Test
    @DisplayName("send wrong key: SearchRoute")
    void wrongKey_searchRoute(){
        odSayClient.changeKey("wrongKey");
        SearchRouteReq searchRouteReq = new SearchRouteReq("126.9707979959352", "37.5547020732267", "127.10012275846414", "37.513264531390575");
        Assertions.assertThrows(IllegalStateException.class, () ->{
            odSayClient.searchRoute(searchRouteReq);
        });
    }
}