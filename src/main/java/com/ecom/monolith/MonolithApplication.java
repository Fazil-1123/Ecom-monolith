package com.ecom.monolith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class MonolithApplication {

	private static final Logger logger = LoggerFactory.getLogger(MonolithApplication.class);

	public static void main(String[] args) {
		var context = SpringApplication.run(MonolithApplication.class, args);
		Environment env = context.getEnvironment();
		logger.info("Application started in '{}' profile", env.getActiveProfiles().length > 0 ? env.getActiveProfiles()[0] : "default");
		logger.info("Application URL: http://localhost:{}", env.getProperty("server.port", "8080"));
	}
}
