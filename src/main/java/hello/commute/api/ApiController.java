package hello.commute.api;

import hello.commute.api.client.GoogleClient;
import hello.commute.api.client.OdSayClient;
import hello.commute.api.client.SeoulClient;
import hello.commute.api.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ApiController {

    private final OdSayClient odSayClient;
    private final SeoulClient seoulClient;
    private final GoogleClient googleClient;
    Map<String, String> errors = new LinkedHashMap<>();

    @GetMapping("/")
    public String home4(Model model){
        model.addAttribute("searchLocationReq", new SearchLocationReq());
        return "searchRouteWithRealTimeInfo";
    }


    @PostMapping("/With-realTimeInfo")
    public String searchRoute3(@ModelAttribute SearchLocationReq searchLocationReq, Model model){
        errors.clear();
        errors = fieldValidation(searchLocationReq);

        if(!errors.isEmpty()){
            model.addAttribute("errors", errors);
            return "searchRouteWithRealTimeInfo";
        }

        ResFactory factory = new ResFactory(searchLocationReq, model, googleClient, odSayClient, seoulClient);
        model = factory.getResult();
        return searchLocationReq.getMiddle().isEmpty()? "resultPage3-1":"resultPage3";
    }

    public Map<String, String> fieldValidation(SearchLocationReq searchLocationReq){
        if (searchLocationReq.getStart().isEmpty()){
            log.info("[ApiController] form 입력값 오류 발생");
            log.info("[ApiController] 'start'is empty" );
            errors.put("start", "'출발지'는 필수 값 입니다.");
        }

        if (searchLocationReq.getEnd().isEmpty()){
            log.info("[ApiController] form 입력값 오류 발생");
            log.info("[ApiController] 'End'is empty");
            errors.put("end", "'목적지'는 필수 값 입니다.");
        }
        return errors;
    }

}
