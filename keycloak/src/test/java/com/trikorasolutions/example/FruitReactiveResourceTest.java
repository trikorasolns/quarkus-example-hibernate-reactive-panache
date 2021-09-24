package com.trikorasolutions.example;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.AccessTokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.Matchers.is;

@QuarkusTest
public class FruitReactiveResourceTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(FruitReactiveResourceTest.class);

  private static final String KEYCLOAK_SERVER_URL = System.getProperty("keycloak.url", "https://localhost:8543/auth");
  private static final String KEYCLOAK_REALM = "quarkus";

  static {
    RestAssured.useRelaxedHTTPSValidation();
  }

  @Test
  public void testPublicResource() {
    RestAssured.given()
      .when().get("/hello/msg/Keycloak")
      .then()
      .statusCode(200)
      .body(is("Hello Keycloak"));
  }

//  @Test
  public void testAccessUserResource() {
    RestAssured.given().auth().oauth2(getAccessToken("alice"))
      .when().get("/api/users/me")
      .then()
      .statusCode(200);
    RestAssured.given().auth().oauth2(getAccessToken("jdoe"))
      .when().get("/api/users/me")
      .then()
      .statusCode(200);
  }

  private String getAccessToken(String userName) {
    return RestAssured
      .given()
      .param("grant_type", "password")
      .param("username", userName)
      .param("password", userName)
      .param("client_id", "backend-service")
      .param("client_secret", "secret")
      .when()
      .post(KEYCLOAK_SERVER_URL + "/realms/" + KEYCLOAK_REALM + "/protocol/openid-connect/token")
      .as(AccessTokenResponse.class).getToken();
  }
}