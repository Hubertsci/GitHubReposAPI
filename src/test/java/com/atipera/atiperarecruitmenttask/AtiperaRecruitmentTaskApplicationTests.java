package com.atipera.atiperarecruitmenttask;

import com.atipera.atiperarecruitmenttask.dto.ReposListDTO;
import com.atipera.atiperarecruitmenttask.dto.RepositoryDTO;
import com.atipera.atiperarecruitmenttask.model.Branch;
import com.atipera.atiperarecruitmenttask.model.Commit;
import com.atipera.atiperarecruitmenttask.model.Owner;
import com.atipera.atiperarecruitmenttask.model.Repository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class AtiperaRecruitmentTaskApplicationTests {

	@RegisterExtension
	static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
			.options(wireMockConfig().dynamicPort().notifier(new ConsoleNotifier(true)))
			.build();

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("github.api.base-url", wireMockExtension::baseUrl);
	}

	@Autowired
	private WebTestClient webTestClient;

	@Test
	public void userNotFoundTest() {
		final String username = "notExistingUser";
		final String gitHubReposEndpoint = "/users/" + username + "/repos";

		wireMockExtension.stubFor(get(gitHubReposEndpoint)
				.willReturn(notFound()
						.withHeader("Content-Type", "application/json")));

		webTestClient
				.get()
				.uri("/api/"+username)
				.header(ACCEPT,APPLICATION_JSON_VALUE)
				.exchange()
				.expectStatus()
				.isEqualTo(NOT_FOUND);
	}

	@Test
	public void happyPathTest() throws JsonProcessingException{
		final String username = "testUser";
		final String repo = "testRepo";
		final String gitHubReposEndpoint = "/users/" + username + "/repos";
		final String gitHubBranchesEndpoint = "/repos/"+username+"/"+repo+"/branches";

		final Commit commit = new Commit("4e825fab11f913ff31e209896776af2b05a2b3da");
		final Branch branch = new Branch("testBranch", commit);
		final List<Branch> branchList = new ArrayList<>();
		branchList.add(branch);
		final Owner owner = new Owner(username);
		final List<Repository> repositoryList = new ArrayList<>();
		repositoryList.add(new Repository(repo, owner));
		final List<RepositoryDTO> repositoryDTOList = new ArrayList<>();
		repositoryDTOList.add(new RepositoryDTO(repo, owner, branchList));
		final ReposListDTO reposListDTO = new ReposListDTO(repositoryDTOList);

		final ObjectMapper objectMapper = new ObjectMapper();

		wireMockExtension.stubFor(get(gitHubReposEndpoint)
				.willReturn(ok()
						.withHeader("Content-Type", "application/json")
						.withBody(objectMapper.writeValueAsString(repositoryList))));

		wireMockExtension.stubFor(get(gitHubBranchesEndpoint)
				.willReturn(ok()
						.withHeader("Content-Type", "application/json")
						.withBody(objectMapper.writeValueAsString(branchList))));

		webTestClient
				.get()
				.uri("/api/" + username)
				.header(ACCEPT,APPLICATION_JSON_VALUE)
				.exchange()
				.expectStatus()
				.isEqualTo(OK)
				.expectBody(ReposListDTO.class)
				.isEqualTo(reposListDTO);

		verify(getRequestedFor(urlEqualTo(gitHubReposEndpoint)));
		verify(getRequestedFor(urlEqualTo(gitHubBranchesEndpoint)));
	}

}
