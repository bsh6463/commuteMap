package hello.commute.api.client;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class OdsayError {

    private Map<String, String> errorMap;

    public OdsayError() {
        errorMap.put("500", "서버 내부 오류");
        errorMap.put("-8", "필수 입력값 형식 및 범위 오류");
        errorMap.put("-9", "필수 입력값 누락");
        errorMap.put("3", "출발지 정류장이 없습니다.");
        errorMap.put("4", "도착지 정류장이 없습니다.");
        errorMap.put("5", "출,도착지 정류장이 없습니다.");
        errorMap.put("6", "서비스 지역이 아닙니다.");
        errorMap.put("-98", "출,도착지가 700m 이내 입니다.");
        errorMap.put("-99", "검색결과가 없습니다.");
        errorMap.put("-11", "검색결과가 없습니다.");


    }
}
