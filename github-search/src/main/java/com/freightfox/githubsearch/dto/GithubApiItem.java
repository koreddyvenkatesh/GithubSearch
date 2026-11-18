package com.freightfox.githubsearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GithubApiItem {
    
    private Long id;
    private String name;
    private String description;
    private String language;
    
    @JsonProperty("stargazers_count")
    private Integer stars;
    
    @JsonProperty("forks_count")
    private Integer forks;
    
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
    
    private Owner owner;

    @Data
    public static class Owner {
        private String login; 
    }
}