package com.atipera.atiperarecruitmenttask.service;

import com.atipera.atiperarecruitmenttask.dto.ReposListDTO;
import com.atipera.atiperarecruitmenttask.model.Repository;

import java.util.List;

public interface GitHubReposService {
    public ReposListDTO getReposList(String gitHubLogin, String headerAccept);
}
