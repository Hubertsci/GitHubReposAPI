package com.atipera.atiperarecruitmenttask.model;

import com.atipera.atiperarecruitmenttask.exception.InternalServerErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

public class Repository {
    private final String repositoryName;
    private final String ownerLogin;
    private final List<Branch> branches;

    public Repository(String repositoryName, String ownerLogin) {
        this.repositoryName = repositoryName;
        this.ownerLogin = ownerLogin;


        this.branches = getBranchList(repositoryName, ownerLogin).stream()
                .map(x -> new Branch(x.get("name").textValue(), x.get("commit").get("sha").textValue()))
                .collect(Collectors.toList());
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public List<Branch> getBranches() {
        return branches;
    }

    private List<JsonNode> getBranchList(String repositoryName, String ownerLogin) {
        String URIString = "https://api.github.com/repos/" + ownerLogin + "/" + repositoryName + "/branches";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URIString))
                .build();
        HttpResponse<String> response;
        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new InternalServerErrorException("Server is not reachable");
        }
        ObjectMapper mapper = new ObjectMapper();

        List<JsonNode> branchList;
        try {
            branchList = mapper.readValue(response.body(), new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new InternalServerErrorException("Something went wrong");
        }

        return branchList;
    }

}
