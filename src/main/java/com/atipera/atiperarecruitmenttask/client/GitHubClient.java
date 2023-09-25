package com.atipera.atiperarecruitmenttask.client;

import com.atipera.atiperarecruitmenttask.exception.GitHubUserNotFoundException;
import com.atipera.atiperarecruitmenttask.exception.InternalServerErrorException;
import com.atipera.atiperarecruitmenttask.model.Branch;
import com.atipera.atiperarecruitmenttask.model.Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GitHubClient {

    private final String gitHubApiBaseUrl;

    public GitHubClient(@Value("${github.api.base-url}") String gitHubApiBaseUrl) {
        this.gitHubApiBaseUrl = gitHubApiBaseUrl;
    }
    public List<Branch> getGitHubBranches(Repository repository) {
        String branches_url = String.format("%1$s/repos/%2$s/%3$s/branches",
                gitHubApiBaseUrl, repository.owner().login(), repository.name());
        WebClient webClient = WebClient.create(branches_url);

        return webClient.get()
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.just(new InternalServerErrorException("Something went wrong")))
                .bodyToFlux(Branch.class)
                .collect(Collectors.toList())
                .block();
    }

    public List<Repository> getGitHubRepositories(String gitHubLogin) {
        String URIString = String.format("%1$s/users/%2$s/repos",
                gitHubApiBaseUrl, gitHubLogin);
        WebClient webClient = WebClient.create(URIString);

        return webClient.get()
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        response -> Mono.just(new GitHubUserNotFoundException("User not found")))
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.just(new InternalServerErrorException("Something went wrong")))
                .bodyToFlux(Repository.class)
                .collect(Collectors.toList())
                .block();
    }
}
