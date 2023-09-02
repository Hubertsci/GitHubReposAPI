package com.atipera.atiperarecruitmenttask.controller;

import com.atipera.atiperarecruitmenttask.exception.BadRequestException;
import com.atipera.atiperarecruitmenttask.exception.NotAcceptableTypeException;
import com.atipera.atiperarecruitmenttask.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class APIController {
    ApplicationService applicationService;

    public APIController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping(value = "{gitHubLogin}")
    public ResponseEntity<Object> getGitHubRepos(@PathVariable("gitHubLogin") String gitHubLogin,
                                         @RequestHeader("Accept") String headerAccept) {
        Set<String> acceptableTypes = new HashSet<>(Arrays.asList("application/json", "application/*", "*/*"));
        if (!acceptableTypes.contains(headerAccept)) {
            throw new NotAcceptableTypeException("Header Accept should contain application/json type");
        }
        if (gitHubLogin == null) {
            throw new BadRequestException("Proper URL should contain: /api/{gitHubUsername}");
        }

        return new ResponseEntity<>(applicationService.getReposList(gitHubLogin), HttpStatus.OK);
    }

}
