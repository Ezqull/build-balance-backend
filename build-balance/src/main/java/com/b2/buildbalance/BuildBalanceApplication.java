package com.b2.buildbalance;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BuildBalanceApplication {

    public static void main(String[] args) {
        final Dotenv dotenv = Dotenv.configure().load();
        System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));
        
        SpringApplication.run(BuildBalanceApplication.class, args);
    }

}
