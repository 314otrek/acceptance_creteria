package com.example.acceptance_criteria.models;

import reactor.core.publisher.Mono;

import java.util.List;

public record RepositoryOutput(String owner, String repositoryName, List<BranchOutput> branches) {
}
