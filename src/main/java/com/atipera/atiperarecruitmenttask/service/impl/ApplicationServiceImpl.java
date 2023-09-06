package com.atipera.atiperarecruitmenttask.service.impl;

import com.atipera.atiperarecruitmenttask.model.Repository;
import com.atipera.atiperarecruitmenttask.service.ApplicationService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Override
    public List<Repository> getReposList(String gitHubLogin) {
        String URIString = "https://api.github.com/users/" + gitHubLogin + "/repos";
        WebClient webClient = WebClient.create(URIString);

        return Arrays.stream(webClient.get()
                .retrieve()
                .bodyToMono(Repository[].class)
                .block()).map(x -> x.addBranches()).collect(Collectors.toList());
    }

}
