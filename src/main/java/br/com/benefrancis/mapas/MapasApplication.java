package br.com.benefrancis.mapas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableRetry //Enable retry
public class MapasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MapasApplication.class, args);
    }

}
