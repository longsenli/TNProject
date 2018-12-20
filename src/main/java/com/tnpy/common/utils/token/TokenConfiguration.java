package com.tnpy.common.utils.token;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration

public class TokenConfiguration implements WebMvcConfigurer {
    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 可以自行筛选
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        return corsConfiguration;
    }
    @Bean
    TokenInterceptor tokenInterceptor() {
        return new TokenInterceptor();
    }
 @Override
public void addInterceptors(InterceptorRegistry registry) {
System.out.println("初始化拦截器");
    //添加拦截器
    //registry.addInterceptor(new JwtInterceptor()).excludePathPatterns("/login","/user/hello");//放掉某些特定不需要校验token的路由
     registry.addInterceptor(tokenInterceptor()).addPathPatterns("/tokentest/**");
     registry.addInterceptor(tokenInterceptor()).addPathPatterns("/api/**");
    }

}

//@Configuration
//public class TokenConfiguration extends WebMvcConfigurerAdapter {
//    @Bean
//    TokenInterceptor tokenInterceptor() {
//        return new TokenInterceptor();
//        // 这个方法才能在拦截器中自动注入查询数据库的对象
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry ){
//        registry.addInterceptor(tokenInterceptor()).addPathPatterns("/tokenteset/**");
//        //配置生成器：添加一个拦截器，拦截路径为API以后的路径
//    }
//}
