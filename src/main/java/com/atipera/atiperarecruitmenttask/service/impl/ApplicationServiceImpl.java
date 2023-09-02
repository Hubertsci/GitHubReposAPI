package com.atipera.atiperarecruitmenttask.service.impl;

import com.atipera.atiperarecruitmenttask.exception.GitHubUserNotFoundException;
import com.atipera.atiperarecruitmenttask.exception.InternalServerErrorException;
import com.atipera.atiperarecruitmenttask.model.Repository;
import com.atipera.atiperarecruitmenttask.service.ApplicationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Override
    public List<Repository> getReposList(String gitHubLogin) {
        String URIString = "https://api.github.com/users/" + gitHubLogin + "/repos";
        HttpRequest myRequest = HttpRequest.newBuilder()
                .uri(URI.create(URIString))
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(myRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new InternalServerErrorException("Server is not reachable");
        }
        if (response.statusCode() == 404) {
            throw new GitHubUserNotFoundException("Requested GitHub user does not exist");
        }

        ObjectMapper mapper = new ObjectMapper();

        List<JsonNode> repos;
        try {
            repos = mapper.readValue(response.body(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException("Something went wrong");
        }

        return repos.stream()
                .filter(x -> !x.get("fork").asBoolean())
                .map(x -> new Repository(x.get("name").textValue(), x.get("owner").get("login").asText()))
                .collect(Collectors.toList());
    }

}
