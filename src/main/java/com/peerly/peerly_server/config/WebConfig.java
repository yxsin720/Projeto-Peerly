package com.peerly.peerly_server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    // Raiz configurável por JVM arg: -Dpeerly.upload-dir=/abs/path
    String root = System.getProperty("peerly.upload-dir", "uploads");
    Path uploadRoot = Paths.get(root).toAbsolutePath().normalize();

    // Tudo que estiver dentro de <root>/ será servido em /files/**
    // Ex.: /files/avatars/xxx.jpg -> <root>/avatars/xxx.jpg
    registry.addResourceHandler("/files/**")
        .addResourceLocations(uploadRoot.toUri().toString() + "/")
        .setCachePeriod(3600); // 1h (opcional)
  }
}
