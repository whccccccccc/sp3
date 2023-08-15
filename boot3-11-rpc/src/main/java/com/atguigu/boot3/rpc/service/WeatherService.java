package com.atguigu.boot3.rpc.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WeatherService {


    //WebClient
    public Mono<String> getWeather(String city) {
        WebClient webClient = WebClient.create("http://ali-weather.showapi.com/hour24");
        return webClient.get().accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "APPCODE a88cd9df7f2d416e8bb0df85804c0cbc")
                .attribute("area",city)
                .retrieve().bodyToMono(String.class);
    }
}
