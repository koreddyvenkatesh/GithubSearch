package com.freightfox.githubsearch.dto;

import lombok.Data;
import java.util.List;

@Data
public class GithubApiSearchResponse {
    
    private List<GithubApiItem> items; 
}