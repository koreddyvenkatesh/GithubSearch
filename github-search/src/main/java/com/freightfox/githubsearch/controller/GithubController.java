package com.freightfox.githubsearch.controller;

import com.freightfox.githubsearch.dto.SearchApiResponse;
import com.freightfox.githubsearch.dto.SearchRequest;
import com.freightfox.githubsearch.model.GithubRepository;
import com.freightfox.githubsearch.service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GithubController {

    private final GithubService githubService;

    
    @PostMapping("/search")
    public ResponseEntity<SearchApiResponse> searchAndSaveRepositories(@RequestBody SearchRequest request) {
        List<GithubRepository> savedRepos = githubService.fetchAndSaveRepositories(request);
        
        SearchApiResponse response = SearchApiResponse.builder()
                .message("Repositories fetched and saved successfully")
                .repositories(savedRepos)
                .build();
                
        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/repositories")
    public ResponseEntity<SearchApiResponse> getStoredRepositories(
            @RequestParam(required = false) String language,
            @RequestParam(required = false) Integer minStars,
            @RequestParam(required = false, defaultValue = "stars") String sort) {
            
        List<GithubRepository> storedRepos = githubService.getStoredRepositories(language, minStars, sort);
        
        SearchApiResponse response = SearchApiResponse.builder()
                .repositories(storedRepos)
                .build();
                
        return ResponseEntity.ok(response);
    }
}