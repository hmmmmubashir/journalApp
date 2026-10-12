package com.engdigest.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;

@Data
public class WeatherResponse {
    private Current current;

    @Data
    public class Current{
        public String observation_time;
        public int temperature;
        @JsonProperty("weather_descriptions")
        public ArrayList<String> weatherDescriptions;
        public int humidity;
        public int feelslike;
        @JsonProperty("uv_index")
        public int uvIndex;
        public int visibility;
    }

}
