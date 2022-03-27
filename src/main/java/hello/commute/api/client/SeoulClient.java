package hello.commute.api.client;

import hello.commute.api.dto.SeoulSubwayArrivalInfoRes;
import hello.commute.api.exception.APIKeyException;
import hello.commute.api.exception.APIServerException;
import hello.commute.api.exception.EndOfServiceException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import static hello.commute.api.client.ErrorType.*;

@Slf4j
@Component
public class SeoulClient {

    @Value(("${seoul.uri}"))
    private String uri;
    private JSONObject jsonResult;
    private Map<String, ErrorType> errorTypeMap;


    public SeoulSubwayArrivalInfoRes getRealtimeInfo(String stationName, String subwayId, String updnLine) {
        stationName= stationName.replace("역", "");
        String uriString = UriComponentsBuilder.fromUriString(uri+stationName).build().toUriString();

        URI uri = UriComponentsBuilder.fromUriString(uriString).encode().build().toUri();

        log.info("[request seoul-subway api] uri = {}", uri);
        //Http Entity
        var httpEntity = new HttpEntity<>(new HttpHeaders());
        var responseType = new ParameterizedTypeReference<String>(){};

        try{
            //ResponseEntity
            var responseEntity= new RestTemplate().exchange(
                    uri, HttpMethod.GET, httpEntity, responseType
            );
            jsonResult = new JSONObject(responseEntity.getBody());

            log.info("[SeoulClient] Station Name: {}", stationName);
            errorCheck(jsonResult);

        }catch (HttpClientErrorException.BadRequest | HttpServerErrorException.InternalServerError exception){
            log.info("[Google Client] Exception 발생: {}", exception.getMessage());
            JSONObject errorInfo = new JSONObject(exception.getResponseBodyAsString());

            errorCheck(errorInfo);
        }
        JSONArray arrivalList = jsonResult.getJSONArray("realtimeArrivalList");
        for (int i=0; i<arrivalList.length(); i++) {
            JSONObject arrival = arrivalList.getJSONObject(i);
            if (isSameWay(subwayId, updnLine, arrival)){
                return new SeoulSubwayArrivalInfoRes((String) arrival.get("arvlMsg2"), (String) arrival.get("trainLineNm"));
            }

        }

        return new SeoulSubwayArrivalInfoRes("요청하신 방향으로 운행이 종료되었습니다.", updnLine);

    }

    private boolean isSameWay(String subwayId, String updnLine, JSONObject arrival) {
        if (subwayId.equals(arrival.get("subwayId"))){
            //같은 호선일 때 다음 역이 같아야 같은 방면임.
            if (updnLine.equals(arrival.get("updnLine"))){
                return true;
            }
        }
        return false;
    }

    private void errorCheck(JSONObject jsonResult) {
        int status=0;
        String code="";
        String message="";

        if (!jsonResult.isNull("errorMessage")){
            JSONObject jsonErrorMessageObject = jsonResult.getJSONObject("errorMessage");
            code = (String) jsonErrorMessageObject.get("code");
            message = (String) jsonErrorMessageObject.get("message");
        }else {
            code = (String) jsonResult.get("code");
            message = (String) jsonResult.get("message");
        }

        log.info("[SeoulClient] Error code: {}", code);
        log.info("[SeoulClient] Error Message: {}", message);
        if (errorTypeMap.get(code) == INVALID_KEY){
            throw new APIKeyException(message);
        }else if (errorTypeMap.get(code) == NO_DATA) {
            throw new NoResultException(message);
        } else if (errorTypeMap.get(code) == USER){
            throw new IllegalArgumentException(message);
        }else if (errorTypeMap.get(code) == SERVER){
            throw new APIServerException(message);
        }else if (errorTypeMap.get(code) == DB){
            throw new APIServerException(message);
        }

    }



    @PostConstruct
    private void setErrorType(){
        errorTypeMap = new LinkedHashMap<>();
        errorTypeMap.put("INFO-000", OK);
        errorTypeMap.put("INFO-100", INVALID_KEY);
        errorTypeMap.put("INFO-200", NO_DATA);
        errorTypeMap.put("ERROR-300", USER);
        errorTypeMap.put("ERROR-301", USER);
        errorTypeMap.put("ERROR-331", USER);
        errorTypeMap.put("ERROR-332", USER);
        errorTypeMap.put("ERROR-333", USER);
        errorTypeMap.put("ERROR-334", USER);
        errorTypeMap.put("ERROR-335", USER);
        errorTypeMap.put("ERROR-336", USER);
        errorTypeMap.put("ERROR-500", SERVER);
        errorTypeMap.put("ERROR-600", DB);
        errorTypeMap.put("ERROR-601", DB);
    }

}
