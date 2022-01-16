package hello.commute.api;

import lombok.extern.slf4j.Slf4j;
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
public class OdSayClient {

    @Value("${odsay.key}")
    private String key;
    @Value(("${odsay.uri}"))
    private String routeSearchUri;

   public String searchRoute(SearchRouteReq searchRouteReq){

       URI uri = UriComponentsBuilder.fromUriString(routeSearchUri)
               .queryParam("SX", searchRouteReq.getSX())
               .queryParam("SY", searchRouteReq.getSY())
               .queryParam("EX", searchRouteReq.getEX())
               .queryParam("EY", searchRouteReq.getEY())
               .queryParam("apiKey", key)
               .build().encode().toUri();

       log.info("[request api] uri = {}", uri);
       //Http Entity
       var httpEntity = new HttpEntity<>(new HttpHeaders());
       var responseType = new ParameterizedTypeReference<String>(){};

       //ResponseEntity
        var responseEntity= new RestTemplate().exchange(
                uri, HttpMethod.GET, httpEntity, responseType
        );

        return responseEntity.getBody();
   }
}
