package hello.commute.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchLocationReq {

    private String start;
    private String end;
    private String middle;


    private MultiValueMap<String, String> makeQuery(){

        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        start = start.replace(" ", "+");
        middle = middle.replace(" ", "+");
        end = end.replace(" ", "+");
        map.add("start", start);
        map.add("middle", middle);
        map.add("end", end);

        return map;
    }

}
