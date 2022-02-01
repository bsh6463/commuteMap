package hello.commute.api.dto;

import hello.commute.api.GoogleClient;
import hello.commute.api.OdSayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;

public class ResFactory {

    private SearchLocationReq searchLocationReq;
    private Model model;
    private GoogleClient googleClient;
    private OdSayClient odSayClient;

    @Autowired
    public ResFactory(SearchLocationReq searchLocationReq, Model model, GoogleClient googleClient, OdSayClient odSayClient) {
        this.searchLocationReq = searchLocationReq;
        this.model = model;
        this.googleClient = googleClient;
        this.odSayClient = odSayClient;
    }

    public Model getResult(){
        //이름 --> 좌표
        JSONObject jsonStartResult = googleClient.searchLocation(searchLocationReq.getStart());
        JSONObject jsonEndResult = googleClient.searchLocation(searchLocationReq.getEnd());

        SearchRouteRes searchRouteRes1;

        if (!searchLocationReq.getMiddle().isEmpty()){
            JSONObject jsonMiddleResult = googleClient.searchLocation(searchLocationReq.getMiddle());
            //시작 -> 경유, 경유 -> 시작 길찾기 req 객체 생성
            SearchRouteReq searchRouteReq1 = getSearchRouteReq(jsonStartResult, jsonMiddleResult);
            SearchRouteReq searchRouteReq2 = getSearchRouteReq(jsonMiddleResult, jsonEndResult);
            searchRouteRes1 = searchRoute3(searchRouteReq1);
            SearchRouteRes searchRouteRes2 = searchRoute3(searchRouteReq2);

            model.addAttribute("result2", searchRouteRes2);
            model.addAttribute("middle", searchLocationReq.getMiddle());
        }else {
            SearchRouteReq searchRouteReq1 = getSearchRouteReq(jsonStartResult, jsonEndResult);
            searchRouteRes1 = searchRoute3(searchRouteReq1);
        }

        //view에 전달.
        model.addAttribute("result1", searchRouteRes1);
        model.addAttribute("start", searchLocationReq.getStart());
        model.addAttribute("end", searchLocationReq.getEnd());

        return model;
    }

    private SearchRouteReq getSearchRouteReq(JSONObject jsonStartResult, JSONObject jsonEndResult) {
        double SX = (double) jsonStartResult.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng");
        double SY = (double) jsonStartResult.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat");
        double EX = (double) jsonEndResult.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng");
        double EY = (double) jsonEndResult.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat");

        SearchRouteReq searchRouteReq = new SearchRouteReq(String.valueOf(SX), String.valueOf(SY), String.valueOf(EX), String.valueOf(EY));
        return searchRouteReq;
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


}