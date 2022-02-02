package hello.commute.api.client;

import hello.commute.api.dto.SearchRealTimeStationReq;
import hello.commute.api.dto.SearchRealTimeStationRes;
import hello.commute.api.dto.SearchRouteReq;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class OdSayClient {

    @Value("${odsay.key}")
    private String key;

    public void changeKey(String key) {
        this.key = key;
    }

    @Value(("${odsay.uri}"))
    private String routeSearchUri;
    @Value(("${odsay.arsId}"))
    private String searchArsIdUri;
    @Value(("${odsay.realTimeStation}"))
    private String realTimeStationUri;

    private static int callCount;

   public JSONObject searchRoute(SearchRouteReq searchRouteReq){
        log.info("callCount : {}", ++callCount);

       String uriString = UriComponentsBuilder.fromUriString(routeSearchUri)
               .queryParam("SX", searchRouteReq.getSX())
               .queryParam("SY", searchRouteReq.getSY())
               .queryParam("EX", searchRouteReq.getEX())
               .queryParam("EY", searchRouteReq.getEY()).encode()
               .queryParam("apiKey", key).build(true).toUriString();

       URI uri = UriComponentsBuilder.fromUriString(uriString).build(true).toUri();
       log.info("[request odsay api] uri = {}", uri);
       //Http Entity
       var httpEntity = new HttpEntity<>(new HttpHeaders());
       var responseType = new ParameterizedTypeReference<String>(){};

       //ResponseEntity
       var responseEntity= new RestTemplate().exchange(
               uri, HttpMethod.GET, httpEntity, responseType
       );
        //log.info("result class : {}", responseEntity.getBody().getClass());

       JSONObject jsonResult = new JSONObject(responseEntity.getBody());
       errorCheck(jsonResult);
       return jsonResult;
   }

    public String getStationId(String stationName, String X, String Y){
       log.info("callCount : {}", ++callCount);

       String uriString = UriComponentsBuilder.fromUriString(searchArsIdUri)
               .queryParam("lang", 0)
               .queryParam("stationName", URLEncoder.encode(stationName, StandardCharsets.UTF_8))
               .queryParam("myLocation", X+":"+Y)
               .queryParam("displayCnt", 1)
               .queryParam("stationClass", 1)
               .queryParam("apiKey", key).build(true).toUriString();

       //log.info("uri String = {}", uriString);

       URI uri = UriComponentsBuilder.fromUriString(uriString).build(true).toUri();
       log.info("[request StationInfo api] uri = {}", uri);

       //Http Entity
       var httpEntity = new HttpEntity<>(new HttpHeaders());
       var responseType = new ParameterizedTypeReference<String>(){};

       //ResponseEntity
       var responseEntity= new RestTemplate().exchange(
               uri, HttpMethod.GET, httpEntity, responseType
       );
       //log.info("result class : {}", responseEntity.getBody().getClass());

       JSONObject jsonResult = new JSONObject(responseEntity.getBody());
       errorCheck(jsonResult);

       int stationIdInt = (int) jsonResult.getJSONObject("result").getJSONArray("station").getJSONObject(0).get("stationID");
       String stationId = String.valueOf(stationIdInt);
       stationId = stationId.replace("-", "");
       return stationId;
   }


   public SearchRealTimeStationRes getRealTimeBusStation(SearchRealTimeStationReq searchRealTimeStationReq){
       log.info("callCount : {}", ++callCount);

       String uriString = UriComponentsBuilder.fromUriString(realTimeStationUri)
               .queryParam("lang", 0)
               .queryParam("stationID", searchRealTimeStationReq.getStationId())
               .queryParam("routeIDs", searchRealTimeStationReq.getBusNumber()).encode()
               .queryParam("apiKey", key).build(true).toUriString();

       URI uri = UriComponentsBuilder.fromUriString(uriString).build(true).toUri();
       log.info("[request realTime Bus Station Info api] uri = {}", uri);

       //Http Entity
       var httpEntity = new HttpEntity<>(new HttpHeaders());
       var responseType = new ParameterizedTypeReference<String>(){};

       //ResponseEntity
       var responseEntity= new RestTemplate().exchange(
               uri, HttpMethod.GET, httpEntity, responseType
       );
       //log.info("result class : {}", responseEntity.getBody().getClass());

       JSONObject jsonResult = new JSONObject(responseEntity.getBody());
       errorCheck(jsonResult);

       return new SearchRealTimeStationRes(jsonResult);
   }

    private void errorCheck(JSONObject jsonResult) {
        //error체크
        if (!jsonResult.getJSONArray("error").isEmpty()){
            JSONObject jsonErrorInfo = jsonResult.getJSONArray("error").getJSONObject(0);
            String errorCode = (String) jsonErrorInfo.get("code");
            String message = (String) jsonErrorInfo.get("message");
            log.info("[ODsay Error] errorCode: {}", errorCode);
            log.info("[ODsay Error] errorMessage: {}", message);

            if (errorCode.equals("500")){
                throw new IllegalStateException(message);
            }else {
                throw new IllegalArgumentException(message);
            }

        }
    }
}
