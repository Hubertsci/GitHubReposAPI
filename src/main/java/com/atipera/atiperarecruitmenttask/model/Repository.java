package com.atipera.atiperarecruitmenttask.model;

import org.springframework.web.reactive.function.client.WebClient;

public record Repository(String name, Owner owner, Branch[] branches) {
    public Repository addBranches() {
        String URIString = "https://api.github.com/repos/" + owner.login() + "/" + name() + "/branches";
        WebClient webClient = WebClient.create(URIString);

        Branch[] newBranches = webClient.get()
                .retrieve()
                .bodyToMono(Branch[].class)
                .block();

        return new Repository(name(), owner(), newBranches);
    }
}
