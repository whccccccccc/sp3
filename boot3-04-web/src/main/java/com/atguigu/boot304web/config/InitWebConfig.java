package com.atguigu.boot304web.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.util.List;

@Configuration
public class InitWebConfig implements WebMvcConfigurer {

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new HttpMessageConverter<Object>() {
            @Override
            public boolean canRead(Class<?> clazz, MediaType mediaType) {
                return false;
            }

            @Override
            public boolean canWrite(Class<?> clazz, MediaType mediaType) {
                return false;
            }

            @Override
            public List<MediaType> getSupportedMediaTypes() {
                return null;
            }

            @Override
            public Object read(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
                return null;
            }

            @Override
            public void write(Object o, MediaType contentType, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

            }
        });
        WebMvcConfigurer.super.extendMessageConverters(converters);
    }
}
