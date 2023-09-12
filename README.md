# GitHub public Repositories API
Given GitHub Username, app returns list of its public repositories.

## Technologies
Java 17\
Spring Boot 3.1.3

## General info

Request given to an API should contain header "Accept: application/json".\
API contains one endpoint "/api/{gitHubUsername}" which returns response in format:
```
[
    {
        "name": ${repositoryName1},
        "owner": {
            "login": ${ownerLogin}
        },
        "branches": [
            {
                "name": ${branchName1},
                "commit": {
                    "sha": ${lastCommitSHA1}
                }
            },

            ...

        ]
    },

   ...

]
```
