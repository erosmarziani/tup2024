package ar.edu.utn.frbb.tup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ar.edu.utn.frbb.tup")
public class Application {

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }


}
