package com.atipera.atiperarecruitmenttask;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class AtiperaRecruitmentTaskApplicationTests {

	@Autowired
	private static WebTestClient webTestClient;

	@BeforeAll
	public static void setup() {
		webTestClient = WebTestClient.bindToServer()
				.baseUrl("http://localhost:8080")
				.build();
	}

	@Test
	public void userNotFoundTest() {
		webTestClient
				.get()
				.uri("/api/")
				.header(ACCEPT,APPLICATION_JSON_VALUE)
				.exchange()
				.expectStatus()
				.isEqualTo(NOT_FOUND);
	}

	@Test
	public void happyPathTest() {
		webTestClient
				.get()
				.uri("/api/Hubertsci")
				.header(ACCEPT,APPLICATION_JSON_VALUE)
				.exchange()
				.expectStatus()
				.isEqualTo(OK);
	}

}
