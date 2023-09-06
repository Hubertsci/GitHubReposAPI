package com.atipera.atiperarecruitmenttask.model;

import com.atipera.atiperarecruitmenttask.exception.InternalServerErrorException;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public record Repository(String name, Owner owner, Branch[] branches) {
    public Repository addBranches() {
        String URIString = "https://api.github.com/repos/" + owner.login() + "/" + name() + "/branches";
        WebClient webClient = WebClient.create(URIString);

        Branch[] newBranches = webClient.get()
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        response -> Mono.just(new InternalServerErrorException("Something went wrong")))
                .bodyToMono(Branch[].class)
                .block();

        return new Repository(name(), owner(), newBranches);
    }
}
