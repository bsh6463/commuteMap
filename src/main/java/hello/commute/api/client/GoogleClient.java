package hello.commute.api.client;

import hello.commute.api.dto.SearchLocationReq;
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

@Slf4j
@Component
public class GoogleClient {

    @Value("${google.key}")
    private String key;
    @Value(("${google.uri}"))
    private String locationUri;

   public JSONObject searchLocation(String location){

       String uriString = UriComponentsBuilder.fromUriString(locationUri)
               .queryParam("address", location)
               .queryParam("key", key).build().toUriString();

       URI uri = UriComponentsBuilder.fromUriString(uriString).build().toUri();

       log.info("[request google api] uri = {}", uri);
       //Http Entity
       var httpEntity = new HttpEntity<>(new HttpHeaders());
       var responseType = new ParameterizedTypeReference<String>(){};

       //ResponseEntity
        var responseEntity= new RestTemplate().exchange(
                uri, HttpMethod.GET, httpEntity, responseType
        );
       // log.info("result class : {}", responseEntity.getBody().getClass());


       return new JSONObject(responseEntity.getBody());
   }
}
