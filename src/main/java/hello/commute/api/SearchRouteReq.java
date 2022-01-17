package hello.commute.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchRouteReq {

    private String SX;
    private String SY;
    private String EX;
    private String EY;


    private MultiValueMap<String, String> makeQuery(){

        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        map.add("SX", SX);
        map.add("SY", SY);
        map.add("EX", EX);
        map.add("EY", EY);

        return map;
    }

}
