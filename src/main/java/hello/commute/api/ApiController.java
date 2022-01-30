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
    public String home4(Model model){
        model.addAttribute("searchLocationReq", new SearchLocationReq());
        return "searchRouteWithRealTimeInfo";
    }


    @PostMapping("/With-realTimeInfo")
    public String searchRoute3(@ModelAttribute SearchLocationReq searchLocationReq, Model model){

        //이름 --> 좌표
        JSONObject jsonStartResult = googleClient.searchLocation(searchLocationReq.getStart());
        JSONObject jsonMiddleResult = googleClient.searchLocation(searchLocationReq.getMiddle());
        JSONObject jsonEndResult = googleClient.searchLocation(searchLocationReq.getEnd());

        //시작 -> 경유, 경유 -> 시작 길찾기 req 객체 생성
        SearchRouteReq searchRouteReq1 = getSearchRouteReq(jsonStartResult, jsonMiddleResult);
        SearchRouteReq searchRouteReq2 = getSearchRouteReq(jsonMiddleResult, jsonEndResult);

        //res객체
        SearchRouteRes searchRouteRes1 = searchRoute3(searchRouteReq1);
        SearchRouteRes searchRouteRes2 = searchRoute3(searchRouteReq2);

        //view에 전달.
        model.addAttribute("result1",  searchRouteRes1);
        model.addAttribute("result2",  searchRouteRes2);
        model.addAttribute("start", searchLocationReq.getStart());
        model.addAttribute("middle", searchLocationReq.getMiddle());
        model.addAttribute("end", searchLocationReq.getEnd());

        return "resultPage3";
    }


    @PostMapping("/With-WayPoint")
    public String searchLocation2(@ModelAttribute SearchLocationReq searchLocationReq, Model model){

        //이름 --> 좌표
        JSONObject jsonStartResult = googleClient.searchLocation(searchLocationReq.getStart());
        JSONObject jsonMiddleResult = googleClient.searchLocation(searchLocationReq.getMiddle());
        JSONObject jsonEndResult = googleClient.searchLocation(searchLocationReq.getEnd());


        //시작 -> 경유, 경유 -> 시작 길찾기 req 객체 생성
        SearchRouteReq searchRouteReq1 = getSearchRouteReq(jsonStartResult, jsonMiddleResult);
        SearchRouteReq searchRouteReq2 = getSearchRouteReq(jsonMiddleResult, jsonEndResult);

        //res객체
        SearchRouteRes searchRouteRes1 = searchRoute3(searchRouteReq1);
        SearchRouteRes searchRouteRes2 = searchRoute3(searchRouteReq2);

        //view에 전달.
        model.addAttribute("result1",  searchRouteRes1);
        model.addAttribute("result2",  searchRouteRes2);
        model.addAttribute("start", searchLocationReq.getStart());
        model.addAttribute("middle", searchLocationReq.getMiddle());
        model.addAttribute("end", searchLocationReq.getEnd());

        return "resultPage2";
    }

    public  SearchRouteRes searchRoute3(@ModelAttribute("searchRouteReq") SearchRouteReq searchRouteReq){
        JSONObject jsonResult = odSayClient.searchRoute(searchRouteReq);
        SearchRouteRes searchRouteRes = new SearchRouteRes(jsonResult, odSayClient);

        ArrayList<Path> pathList = getPathList(searchRouteRes);
        //api로 얻은 pathList를 대체함.
        searchRouteRes.setPathList(pathList);

        return searchRouteRes;
    }

    private ArrayList<Path> getPathList(SearchRouteRes searchRouteRes) {
        //여러 path중 2개만 사용.
        ArrayList<Path> pathList = new ArrayList<>();
        pathList.add(searchRouteRes.getPathList().get(0));
        pathList.add(searchRouteRes.getPathList().get(1));

        //2개의 path에 대해 realtime 정보를 가져옴.
        for (Path path : pathList) {
            for (SubPath subPath : path.getSubPathList()) {
                subPath.getStationIdAndRealTimeInfo(odSayClient);
            }
        }
        return pathList;
    }


    private SearchRouteReq getSearchRouteReq(JSONObject jsonStartResult, JSONObject jsonEndResult) {
        double SX = (double) jsonStartResult.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng");
        double SY = (double) jsonStartResult.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat");
        double EX = (double) jsonEndResult.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng");
        double EY = (double) jsonEndResult.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat");

        SearchRouteReq searchRouteReq = new SearchRouteReq(String.valueOf(SX), String.valueOf(SY), String.valueOf(EX), String.valueOf(EY));
        return searchRouteReq;
    }


    /*
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

    //@PostMapping("/search")
    public Model searchRoute2(@ModelAttribute("searchRouteReq") SearchRouteReq searchRouteReq, Model model){
        JSONObject jsonResult = odSayClient.searchRoute(searchRouteReq);
        SearchRouteRes searchRouteRes = new SearchRouteRes(jsonResult);
        model.addAttribute("result", searchRouteRes);
        model.addAttribute("pathList", searchRouteRes.getPathList());
        //result page로 이동.
        return model;
    }*/


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
