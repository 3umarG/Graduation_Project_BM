package com.bm.graduationproject;

import com.bm.graduationproject.repositories.CurrencyRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.bm.graduationproject.repositories")
public class GraduationProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraduationProjectApplication.class, args);
    }

}
