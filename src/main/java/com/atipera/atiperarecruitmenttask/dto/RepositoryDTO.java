package com.atipera.atiperarecruitmenttask.dto;

import com.atipera.atiperarecruitmenttask.model.Branch;
import com.atipera.atiperarecruitmenttask.model.Owner;

import java.util.List;

public record RepositoryDTO(String name, Owner owner, List<Branch> branches) {
}
