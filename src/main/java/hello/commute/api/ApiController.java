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

       ResFactory factory = new ResFactory(searchLocationReq, model, googleClient, odSayClient);
       model = factory.getResult();

        return searchLocationReq.getMiddle().isEmpty()? "resultPage3-1":"resultPage3";
    }

}
