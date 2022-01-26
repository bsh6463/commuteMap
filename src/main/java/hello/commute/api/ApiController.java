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
    public String home2(Model model){
        model.addAttribute("searchLocationReq", new SearchLocationReq());
        return "searchLocationForm";
    }

    @PostMapping("/By-locationName")
    public String searchLocation(@ModelAttribute SearchLocationReq searchLocationReq, Model model){
        JSONObject jsonStartResult = googleClient.searchLocation(searchLocationReq.getStart());
        JSONObject jsonEndResult = googleClient.searchLocation(searchLocationReq.getEnd());

        SearchRouteReq searchRouteReq = getSearchRouteReq(jsonStartResult, jsonEndResult);

        model = searchRoute2(searchRouteReq, model);

        return "resultPage";
    }

    private SearchRouteReq getSearchRouteReq(JSONObject jsonStartResult, JSONObject jsonEndResult) {
        double SX = (double) jsonStartResult.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng");
        double SY = (double) jsonStartResult.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat");
        double EX = (double) jsonEndResult.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng");
        double EY = (double) jsonEndResult.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat");

        SearchRouteReq searchRouteReq = new SearchRouteReq(String.valueOf(SX), String.valueOf(SY), String.valueOf(EX), String.valueOf(EY));
        return searchRouteReq;
    }

    //@PostMapping("/search")
    public Model searchRoute2(@ModelAttribute("searchRouteReq") SearchRouteReq searchRouteReq, Model model){
        JSONObject jsonResult = odSayClient.searchRoute(searchRouteReq);
        SearchRouteRes searchRouteRes = new SearchRouteRes(jsonResult);
        model.addAttribute("result", searchRouteRes);
        model.addAttribute("pathList", searchRouteRes.getPathList());
        //result page로 이동.
        return model;
    }


    /*@GetMapping("/")
    public String home(Model model){
        model.addAttribute("searchRouteReq", new SearchRouteReq());
        return "searchForm";
    }*/

    /*
    //@PostMapping("/search")
    public String searchRoute(@ModelAttribute("searchRouteReq") SearchRouteReq searchRouteReq, Model model){
        JSONObject jsonResult = odSayClient.searchRoute(searchRouteReq);
        SearchRouteRes searchRouteRes = new SearchRouteRes(jsonResult);
        model.addAttribute("result", searchRouteRes);
        model.addAttribute("pathList", searchRouteRes.getPathList());
        //result page로 이동.
        return "resultPage";
    }*/

}
