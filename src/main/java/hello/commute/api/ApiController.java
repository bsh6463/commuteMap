package hello.commute.api;

import hello.commute.api.dto.*;
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
    private final GoogleClient googleClient;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("searchLocationReq", new SearchLocationReq());
        return "searchLocationForm";
    }

    @PostMapping("/location")
    public String searchLocation(@ModelAttribute SearchLocationReq searchLocationReq, Model model){
        JSONObject jsonStartResult = googleClient.searchLocation(searchLocationReq.getStart());
        JSONObject jsonEndResult = googleClient.searchLocation(searchLocationReq.getEnd());

        int SX = (int) jsonStartResult.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lng");
        int SY = (int) jsonStartResult.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lat");
        int EX = (int) jsonEndResult.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lng");
        int EY = (int) jsonEndResult.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lat");

        SearchRouteReq searchRouteReq = new SearchRouteReq(String.valueOf(SX), String.valueOf(SY), String.valueOf(EX), String.valueOf(EY));

        model = searchRoute(searchRouteReq, model);

        return "resultPage";
    }

    //@PostMapping("/search")
    public Model searchRoute(@ModelAttribute("searchRouteReq") SearchRouteReq searchRouteReq, Model model){
        JSONObject jsonResult = odSayClient.searchRoute(searchRouteReq);
        SearchRouteRes searchRouteRes = new SearchRouteRes(jsonResult);
        model.addAttribute("result", searchRouteRes);
        model.addAttribute("pathList", searchRouteRes.getPathList());
        //result page로 이동.
        return model;
    }

}
