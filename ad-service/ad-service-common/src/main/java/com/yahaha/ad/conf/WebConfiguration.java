package com.yahaha.ad.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 通过实现WebMvcConfigurer接口，不光可以实现消息转换器，开可以添加拦截器，进行跨域配置，资源路径配置
 * 自定义视图解析器
 * @Auther LeeMZ
 * @Date 2021/2/6
 **/
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    /**
     * 消息转换器
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.clear();
        converters.add(new MappingJackson2HttpMessageConverter()); //可以实现将java对象转成一个json对象
    }
}
