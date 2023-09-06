package com.atipera.atiperarecruitmenttask.service.impl;

import com.atipera.atiperarecruitmenttask.exception.GitHubUserNotFoundException;
import com.atipera.atiperarecruitmenttask.exception.InternalServerErrorException;
import com.atipera.atiperarecruitmenttask.exception.NotAcceptableTypeException;
import com.atipera.atiperarecruitmenttask.model.Repository;
import com.atipera.atiperarecruitmenttask.service.ApplicationService;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Override
    public List<Repository> getReposList(String gitHubLogin, String headerAccept) {
        String URIString = "https://api.github.com/users/" + gitHubLogin + "/repos";
        WebClient webClient = WebClient.create(URIString);

        return Arrays.stream(webClient.get()
                .header("Accept", headerAccept)
                .retrieve()
                .onStatus(httpStatus -> httpStatus.value() == 415,
                        response -> Mono.just(new NotAcceptableTypeException("Unsupported 'Accept' header: 'application/xml'. Must accept 'application/json'.")))
                .onStatus(httpStatus -> httpStatus.value() == 404,
                        response -> Mono.just(new GitHubUserNotFoundException("GitHub User not found")))
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.just(new InternalServerErrorException("Something went wrong")))
                .bodyToMono(Repository[].class)
                .block()).map(Repository::addBranches).collect(Collectors.toList());
    }

}
