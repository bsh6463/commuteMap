package hello.commute;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommuteApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommuteApplication.class, args);
        System.out.println("hello");
    }


}
