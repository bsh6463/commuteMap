package hello.commute.api;

import hello.commute.api.dto.Path;
import hello.commute.api.dto.SearchRouteReq;
import hello.commute.api.dto.SearchRouteRes;
import hello.commute.api.dto.SubPath;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ApiController {

    private final OdSayClient odSayClient;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("req", new SearchRouteReq());
        return "searchForm";
    }

    @PostMapping("/search")
    public String searchRoute(@ModelAttribute SearchRouteReq req, Model model){
        //서율역
         req.setSX("126.9707979959352");
         req.setSY("37.5547020732267");
        //잠실역
         req.setEX("127.10012275846414");
         req.setEY("37.513264531390575");

        String result = odSayClient.searchRoute(req);
        //log.info("[result] : {}", result);
        JSONObject jsonResult = new JSONObject(result);
        SearchRouteRes searchRouteRes = new SearchRouteRes(jsonResult);
        model.addAttribute("result", searchRouteRes);
        model.addAttribute("pathList", searchRouteRes.getPathList());
        //result page로 이동.
        return "resultPage";
    }


}
