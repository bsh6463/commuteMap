package hello.commute.api.client;


import hello.commute.api.dto.SearchRealTimeStationReq;
import hello.commute.api.dto.SearchRouteReq;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureMockMvc
class OdSayClientTest {
    @Autowired OdSayClient odSayClient;
    @Value("${odsay.key}")
    private String key;

    @AfterEach
    public void rollbackKey(){
        odSayClient.changeKey(key);
    }

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

    @Test
    @DisplayName("send wrong key: RealTime Info")
    void wrongKey_realTimeInfo(){
        odSayClient.changeKey("wrongKey");
        SearchRealTimeStationReq searchRealTimeStationReq = new SearchRealTimeStationReq("102155", 1210);
        Assertions.assertThrows(IllegalStateException.class, () ->{
            odSayClient.getRealTimeBusStation(searchRealTimeStationReq);
        });
    }

   @Test
    @DisplayName("send Empty Data : getStationId")
    void emptyData_getStationId(){
        Assertions.assertThrows(IllegalArgumentException.class, () ->{
            odSayClient.getStationId("", "","");
        });
    }

    @Test
    @DisplayName("send Empty Data: SearchRoute --> errorCode: null")
    void emptyData_searchRoute(){
        SearchRouteReq searchRouteReq = new SearchRouteReq("", "", "", "");
        Assertions.assertThrows(IllegalStateException.class, () ->{
            odSayClient.searchRoute(searchRouteReq);
        });
    }

    @Test
    @DisplayName("send Empty Data: RealTime Info")
    void emptyData_realTimeInfo(){
        SearchRealTimeStationReq searchRealTimeStationReq = new SearchRealTimeStationReq("", null);
        Assertions.assertThrows(IllegalArgumentException.class, () ->{
            odSayClient.getRealTimeBusStation(searchRealTimeStationReq);
        });
    }
}