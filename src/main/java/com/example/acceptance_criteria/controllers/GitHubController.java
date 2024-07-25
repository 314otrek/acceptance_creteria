package com.example.acceptance_criteria.controllers;

import com.example.acceptance_criteria.exceptions.UserNotFoundException;
import com.example.acceptance_criteria.models.*;
import com.example.acceptance_criteria.services.GitHubService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GitHubController {

    private final GitHubService gitHubService;

    @GetMapping("get/{username}")
    public Mono<ResultJson> getJsonResult(@PathVariable String username){
        return  gitHubService.getResultData(username);
    }


    @GetMapping("getInfo/{user}")
    public Flux<RepositoryFromJson> getRepoInfo(@PathVariable String user){
        return gitHubService.getUserRepositories(user);
    }



}
