package com.example.acceptance_criteria.services;

import com.example.acceptance_criteria.exceptions.UserNotFoundException;
import com.example.acceptance_criteria.models.*;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GitHubService {

    private final WebClient webClient;

    private GitHubService(WebClient.Builder webClient) {
        this.webClient = webClient
                .baseUrl("https://api.github.com")
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

//return list of Repositories
    public Flux<RepositoryFromJson> getUserRepositories(String userName) {
        return webClient.get()
                .uri(uri -> uri
                        .path("/users/{username}/repos")
                        .queryParam("type", "source")
                        .build(userName))
                .retrieve()
                .bodyToFlux(RepositoryFromJson.class)
                .filter(repoInfo -> !repoInfo.fork());

    }

    public Flux<BranchOutput> getRepositoryBranches(String userName, String repository){
        return webClient.get()
                .uri("repos/{owner}/{repo}/branches",userName,repository)
                .retrieve()
                .bodyToFlux(GitHubBranch.class)
                .map(branch -> new BranchOutput(branch.name(),branch.commit().sha()));

    }

    public Mono<ResultJson> getResultData(String username) {

        return getUserRepositories(username)
                .flatMap(repo -> getRepositoryBranches(username, repo.name())
                        .collectList()
                        .map(branches -> new RepositoryOutput(repo.owner().login(),repo.name(),branches)))
                .collectList()
                .map(ResultJson::new)
                .onErrorResume(WebClientResponseException.NotFound.class,
                        exception -> Mono.error(new UserNotFoundException("User "+username+" not Found")));
    }
}
