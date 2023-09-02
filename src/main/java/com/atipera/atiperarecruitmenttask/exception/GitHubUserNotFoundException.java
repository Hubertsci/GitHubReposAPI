package com.atipera.atiperarecruitmenttask.exception;

public class GitHubUserNotFoundException extends RuntimeException{
    public GitHubUserNotFoundException(String message) {
        super(message);
    }
}
