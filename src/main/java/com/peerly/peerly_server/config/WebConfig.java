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
    
    String root = System.getProperty("peerly.upload-dir", "uploads");
    Path uploadRoot = Paths.get(root).toAbsolutePath().normalize();

    
    registry.addResourceHandler("/files/**")
        .addResourceLocations(uploadRoot.toUri().toString() + "/")
        .setCachePeriod(3600); 
  }
}
