package com.example.acceptance_criteria.models;

import reactor.core.publisher.Mono;

import java.util.List;

public record ResultJson(List<RepositoryOutput> repositories) {
}
