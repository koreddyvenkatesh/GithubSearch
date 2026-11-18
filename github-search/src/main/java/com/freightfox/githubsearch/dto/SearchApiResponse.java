package com.freightfox.githubsearch.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.freightfox.githubsearch.model.GithubRepository;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class SearchApiResponse {
    private String message;
    private List<GithubRepository> repositories;
}