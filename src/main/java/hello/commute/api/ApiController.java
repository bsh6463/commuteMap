package hello.commute.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ApiController {

    private final OdSayClient odSayClient;

    @GetMapping("/route")
    public String searchRoute(){
        //서율역
        String SX ="126.9707979959352";
        String SY = "37.5547020732267";
        //잠실역
        String EX = "127.10012275846414";
        String EY = "37.513264531390575";

        SearchRouteReq searchRouteReq = new SearchRouteReq(SX, SY, EX, EY);
        String result = odSayClient.searchRoute(searchRouteReq);
        JSONObject jsonResult = new JSONObject(result);

        return "ok";
    }


}
