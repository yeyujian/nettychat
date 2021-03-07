package com.yyj.nettychat.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.servlet.MultipartConfigElement;

//@Component
public class FastDFSConfig {
//    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize("70120KB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("70120KB");
        return factory.createMultipartConfig();
    }
}
