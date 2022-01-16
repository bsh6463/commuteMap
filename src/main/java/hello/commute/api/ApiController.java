package hello.commute.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        String SX ="126.9707979959352";
        String SY = "37.5547020732267";
        String EX = "127.10012275846414";
        String EY = "37.513264531390575";
        //서율역 위도 경도,
        //y:37.5547020732267, x:126.9707979959352
        //잠실역
        //y:37.513264531390575, x: 127.10012275846414
        SearchRouteReq searchRouteReq = new SearchRouteReq(SX, SY, EX, EY);
        String result = odSayClient.searchRoute(searchRouteReq);

        System.out.println(result);
        return result;
    }


}
