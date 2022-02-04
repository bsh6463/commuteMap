package hello.commute.api.client;

import lombok.extern.slf4j.Slf4j;
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

import javax.persistence.NoResultException;
import java.net.URI;

@Slf4j
@Component
public class GoogleClient {

    @Value("${google.key}")
    private String key;
    @Value(("${google.uri}"))
    private String locationUri;
    private JSONObject jsonResult;

    public void changeKey(String key) {
        this.key = key;
    }

    public JSONObject searchLocation(String location) {
        String status="";

       String uriString = UriComponentsBuilder.fromUriString(locationUri)
               .queryParam("address", location)
               .queryParam("key", key).build().toUriString();

       URI uri = UriComponentsBuilder.fromUriString(uriString).build().toUri();

       log.info("[request google api] uri = {}", uri);
       //Http Entity
       var httpEntity = new HttpEntity<>(new HttpHeaders());
       var responseType = new ParameterizedTypeReference<String>(){};

        try{
            //ResponseEntity
            var responseEntity= new RestTemplate().exchange(
                    uri, HttpMethod.GET, httpEntity, responseType
            );
            jsonResult = new JSONObject(responseEntity.getBody());
            status = (String) jsonResult.get("status");
            if (!status.equals("OK")){
                errorCheck(jsonResult, status);
            }
        }catch (HttpClientErrorException.BadRequest | HttpServerErrorException.InternalServerError exception){
            log.info("[Google Client] Exception 발생: {}", exception.getMessage());
            JSONObject errorInfo = new JSONObject(exception.getResponseBodyAsString());
            errorCheck(errorInfo, exception.getStatusCode().name());
        }

        return jsonResult;

    }

    private void errorCheck(JSONObject jsonResult, String status) {
        if ( status.equals("UNKNOWN_ERROR")){
            String errorMessage = (String) jsonResult.get("error_message");
            log.info("[Google Client Error] Status: {}", status);
            log.info("[Google Client Error] errorMessage: {}", errorMessage);
            throw new IllegalStateException(errorMessage);
        }else if (status.equals("ZERO_RESULTS")){
            log.info("[Google Client Error] Status: {}", status);
            throw new NoResultException("결과가 없습니다.");
        } else {
            String errorMessage = (String) jsonResult.get("error_message");
            log.info("[Google Client Error] Status: {}", status);
            log.info("[Google Client Error] errorMessage: {}", errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
