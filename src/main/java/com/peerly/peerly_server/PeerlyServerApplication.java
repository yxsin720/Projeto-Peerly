package com.peerly.peerly_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.peerly.peerly_server")
@EntityScan(basePackages = "com.peerly.peerly_server.models")
@EnableJpaRepositories(basePackages = "com.peerly.peerly_server.repositories")
public class PeerlyServerApplication {
  public static void main(String[] args) {
    SpringApplication.run(PeerlyServerApplication.class, args);
  }
}
