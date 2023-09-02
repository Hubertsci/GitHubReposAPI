package com.atipera.atiperarecruitmenttask.model;

public class Branch {
    private final String branchName;
    private final String lastCommitSHA;

    public Branch(String branchName, String lastCommitSHA) {
        this.branchName = branchName;
        this.lastCommitSHA = lastCommitSHA;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getLastCommitSHA() {
        return lastCommitSHA;
    }
}
