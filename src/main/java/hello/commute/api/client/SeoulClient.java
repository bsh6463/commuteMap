package hello.commute.api.client;

import hello.commute.api.exception.APIKeyException;
import hello.commute.api.exception.APIServerException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

    public JSONObject getRealtimeInfo(String stationName) {

        String uriString = UriComponentsBuilder.fromUriString(uri+stationName).build().toUriString();

        URI uri = UriComponentsBuilder.fromUriString(uriString).build().toUri();

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
            int status = (int) jsonResult.getJSONObject("errorMessage").get("status");
            if (status!=200){
                errorCheck(jsonResult);
            }
        }catch (HttpClientErrorException.BadRequest | HttpServerErrorException.InternalServerError exception){
            log.info("[Google Client] Exception 발생: {}", exception.getMessage());
            JSONObject errorInfo = new JSONObject(exception.getResponseBodyAsString());
            errorCheck(errorInfo);
        }

        return jsonResult;

    }

    @After("seoulClient()")
    private void errorCheck(JSONObject jsonResult) {
        JSONObject jsonErrorMessageObject = jsonResult.getJSONObject("errorMessage");
        String code = (String) jsonErrorMessageObject.get("code");
        String message = (String) jsonErrorMessageObject.get("message");
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
