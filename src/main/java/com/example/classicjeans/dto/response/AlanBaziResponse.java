package com.example.classicjeans.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlanBaziResponse {
    @JsonProperty(value = "action")
    private Action action;

    @JsonProperty(value = "content")
    private String content;

    @Getter
    static class Action {

        @JsonProperty(value = "name")
        private String name;

        @JsonProperty(value = "speak")
        private String speak;
    }
}
