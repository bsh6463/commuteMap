package hello.commute.api.dto;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchLocationReq {

    @NotNull
    private String start;
    @NotNull
    private String end;
    @Nullable
    private String middle;


    private MultiValueMap<String, String> makeQuery(){

        LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();

        start = start.replace(" ", "+");
        end = end.replace(" ", "+");
        map.add("start", start);
        map.add("end", end);

        if(this.middle != null){
            map.add("middle", middle);
            middle = middle.replace(" ", "+");
        }

        return map;
    }

}
