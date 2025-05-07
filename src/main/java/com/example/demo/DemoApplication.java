package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.demo.repository")
public class DemoApplication {
	
	public static final String APP_NAME = "secure-web-login-demo ";
	public static String APP_VERSION = null;
	
    public static void main(String[] args) {
    	
    	System.setProperty("log4j.configurationFile", "log4j2.xml");
    	
        SpringApplication.run(DemoApplication.class, args);
    }
    
}
