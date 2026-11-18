package com.freightfox.githubsearch.service;

import com.freightfox.githubsearch.dto.GithubApiSearchResponse;
import com.freightfox.githubsearch.dto.SearchRequest;
import com.freightfox.githubsearch.model.GithubRepository;
import com.freightfox.githubsearch.repository.GithubRepoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GithubService {

    private final GithubRepoRepository repository;
    private final RestTemplate restTemplate;

    private static final String GITHUB_API_URL = "https://api.github.com/search/repositories";

  
    @Transactional
    public List<GithubRepository> fetchAndSaveRepositories(SearchRequest request) {
        try {
            
            String queryParam = request.getQuery();
            if (request.getLanguage() != null && !request.getLanguage().isEmpty()) {
                queryParam += "+language:" + request.getLanguage();
            }

            
            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(GITHUB_API_URL)
                    .queryParam("q", queryParam);

            if (request.getSort() != null && !request.getSort().isEmpty()) {
                uriBuilder.queryParam("sort", request.getSort());
            }

            String finalUrl = uriBuilder.build().toUriString();

            
            ResponseEntity<GithubApiSearchResponse> response = restTemplate.getForEntity(
                    finalUrl, GithubApiSearchResponse.class);

            if (response.getBody() == null || response.getBody().getItems() == null) {
                return new ArrayList<>();
            }

            
            List<GithubRepository> repositoriesToSave = response.getBody().getItems().stream()
                    .map(item -> new GithubRepository(
                            item.getId(),
                            item.getName(),
                            item.getDescription(),
                            item.getOwner() != null ? item.getOwner().getLogin() : null,
                            item.getLanguage(),
                            item.getStars(),
                            item.getForks(),
                            item.getUpdatedAt()
                    ))
                    .collect(Collectors.toList());

            
            return repository.saveAll(repositoriesToSave);

        } catch (HttpClientErrorException e) {
            
            throw new RuntimeException("Failed to fetch data from GitHub API: " + e.getMessage());
        }
    }

    public List<GithubRepository> getStoredRepositories(String language, Integer minStars, String sortBy) {
        
        String sortProperty = "stars"; 
        if (sortBy != null) {
            switch (sortBy.toLowerCase()) {
                case "forks": sortProperty = "forks"; break;
                case "updated": sortProperty = "lastUpdated"; break;
            }
        }
        
        
        Sort sort = Sort.by(Sort.Direction.DESC, sortProperty);

        return repository.findWithFilters(language, minStars, sort);
    }
}