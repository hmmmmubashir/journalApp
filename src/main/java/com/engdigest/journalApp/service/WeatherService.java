package com.engdigest.journalApp.service;

import com.engdigest.journalApp.api.response.WeatherResponse;
import com.engdigest.journalApp.cache.AppCache;
import com.engdigest.journalApp.entity.ConfigEntity;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${weather_api_key}")
    private String api_key;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AppCache appCache;

    @Autowired
    RedisService redisService;

    public WeatherResponse getWeather(String city){
        WeatherResponse weatherResponse=redisService.get(city,WeatherResponse.class);
        if(weatherResponse!=null)
            return weatherResponse;
        else{
            String final_api=appCache.cache.get("weather_api").replace("<city>",city).replace("<apikey>",api_key);
            ResponseEntity<WeatherResponse> response=restTemplate.exchange(final_api, HttpMethod.GET,null, WeatherResponse.class);
            WeatherResponse body= response.getBody();
            if(body!=null)
                redisService.set(city,body,300l);
            return body;
        }
    }
}
