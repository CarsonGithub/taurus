package com.code.taurus.common.config;

import com.code.taurus.common.constant.CommonConstant;
import com.code.taurus.common.service.impl.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * web配置
 *
 * @author 郑楷山
 **/

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private FileService fileService;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry
                .addResourceHandler(CommonConstant.UPLOAD_FILE_PATH + "/**")
                .addResourceLocations("file:" + fileService.getFileRootPath()+"/");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
