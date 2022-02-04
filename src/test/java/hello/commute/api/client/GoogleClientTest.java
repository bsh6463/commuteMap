package hello.commute.api.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.NoResultException;

@SpringBootTest
class GoogleClientTest {
    @Value("${google.key}")
    private String key;

    @Autowired
    GoogleClient googleClient;

    @AfterEach
    void rollbackKey(){
        googleClient.changeKey(key);
    }

    @Test
    @DisplayName("send wrong key")
    void wrongKey(){
        googleClient.changeKey("wrong Key");
        Assertions.assertThrows(IllegalArgumentException.class, () ->{googleClient.searchLocation("서울역");});
    }

    @Test
    @DisplayName("No Result")
    void noResult(){
        Assertions.assertThrows(NoResultException.class, () ->{googleClient.searchLocation("a5dsQ2");});
    }

    @Test
    @DisplayName("empty data")
    void emptyData(){
        Assertions.assertThrows(IllegalArgumentException.class, () ->{googleClient.searchLocation("");});
    }
}