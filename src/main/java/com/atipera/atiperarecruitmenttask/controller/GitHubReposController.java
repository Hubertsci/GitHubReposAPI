package com.atipera.atiperarecruitmenttask.controller;

import com.atipera.atiperarecruitmenttask.dto.ReposListDTO;
import com.atipera.atiperarecruitmenttask.service.GitHubReposService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class GitHubReposController {
    private final GitHubReposService gitHubReposService;

    public GitHubReposController(final GitHubReposService gitHubReposService) {
        this.gitHubReposService = gitHubReposService;
    }

    @GetMapping(value = "{gitHubLogin}")
    public ReposListDTO getGitHubRepos(@PathVariable("gitHubLogin") String gitHubLogin,
                                       @RequestHeader("Accept") String headerAccept) {
        return gitHubReposService.getReposList(gitHubLogin, headerAccept);
    }

}
