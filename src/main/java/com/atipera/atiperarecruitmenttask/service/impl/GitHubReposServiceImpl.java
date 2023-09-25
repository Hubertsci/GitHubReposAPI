package com.atipera.atiperarecruitmenttask.service.impl;

import com.atipera.atiperarecruitmenttask.client.GitHubClient;
import com.atipera.atiperarecruitmenttask.dto.ReposListDTO;
import com.atipera.atiperarecruitmenttask.dto.RepositoryDTO;
import com.atipera.atiperarecruitmenttask.model.Branch;
import com.atipera.atiperarecruitmenttask.model.Repository;
import com.atipera.atiperarecruitmenttask.service.GitHubReposService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubReposServiceImpl implements GitHubReposService {
    private final GitHubClient gitHubClient;

    public GitHubReposServiceImpl(final GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    @Override
    public ReposListDTO getReposList(String gitHubLogin, String headerAccept) {
        return ReposListDTOMapper(
            gitHubClient.getGitHubRepositories(gitHubLogin).stream()
                    .map(x -> RepositoryDTOMapper(x, gitHubClient.getGitHubBranches(x)))
                    .collect(Collectors.toList()));
    }

    private RepositoryDTO RepositoryDTOMapper(Repository repository, List<Branch> branches) {
        return new RepositoryDTO(repository.name(), repository.owner(), branches);
    }

    private ReposListDTO ReposListDTOMapper(List<RepositoryDTO> repositoryDTOs) {
        return new ReposListDTO(repositoryDTOs);
    }
}
