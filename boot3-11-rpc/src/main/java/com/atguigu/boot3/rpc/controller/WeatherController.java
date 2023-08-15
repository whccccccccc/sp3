package com.atguigu.boot3.rpc.controller;

import com.atguigu.boot3.rpc.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @GetMapping("/weather/{city}")
    public Mono<String> getWeather(@PathVariable String city) {
        log.info("getWeather:{}", city);
        return weatherService.getWeather(city);
    }
}
