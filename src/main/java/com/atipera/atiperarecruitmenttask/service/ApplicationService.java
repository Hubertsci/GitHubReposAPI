package com.atipera.atiperarecruitmenttask.service;

import com.atipera.atiperarecruitmenttask.model.Repository;

import java.util.List;

public interface ApplicationService {
    public List<Repository> getReposList(String gitHubLogin);
}
