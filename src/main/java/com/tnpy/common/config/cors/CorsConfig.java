package com.tnpy.common.config.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class CorsConfig extends WebMvcConfigurerAdapter {
@Override
public void addCorsMappings(CorsRegistry registry) {
	//System.out.println("拦截器设置！CorsConfig");
	registry.addMapping("/**").allowedOrigins("*")
			.allowedMethods("GET", "HEAD", "POST","PUT", "DELETE", "OPTIONS")
			.allowCredentials(true).maxAge(3600);
}
}





