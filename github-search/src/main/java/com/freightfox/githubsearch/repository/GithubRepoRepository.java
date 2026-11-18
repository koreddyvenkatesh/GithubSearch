package com.freightfox.githubsearch.repository;

import com.freightfox.githubsearch.model.GithubRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GithubRepoRepository extends JpaRepository<GithubRepository, Long> {

    
    @Query("SELECT g FROM GithubRepository g WHERE " +
           "(:language IS NULL OR g.language = :language) AND " +
           "(:minStars IS NULL OR g.stars >= :minStars)")
    List<GithubRepository> findWithFilters(@Param("language") String language, 
                                           @Param("minStars") Integer minStars, 
                                           Sort sort);
}