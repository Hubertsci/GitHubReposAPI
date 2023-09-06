package com.atipera.atiperarecruitmenttask.controller;

import com.atipera.atiperarecruitmenttask.model.Repository;
import com.atipera.atiperarecruitmenttask.service.ApplicationService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class APIController {
    private final ApplicationService applicationService;

    public APIController(final ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping(value = "{gitHubLogin}")
    public List<Repository> getGitHubRepos(@PathVariable("gitHubLogin") String gitHubLogin,
                                           @RequestHeader("Accept") String headerAccept) {
        return applicationService.getReposList(gitHubLogin, headerAccept);
    }

}
