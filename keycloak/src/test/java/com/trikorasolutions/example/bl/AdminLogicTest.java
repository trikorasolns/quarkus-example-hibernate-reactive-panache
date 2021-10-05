package com.trikorasolutions.example.bl;

import com.trikorasolutions.example.dto.UserDto;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.trikorasolutions.example.bl.KeycloakInfo.getAccessToken;
import static javax.ws.rs.core.Response.Status.OK;

@QuarkusTest
public class AdminLogicTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(AdminLogicTest.class);

  @Test
  public void testListKeycloakUsers() {

    RestAssured.given().auth().oauth2(getAccessToken("admin"))
      .when().get("/api/admin/listUsers/trikorasolutions")
      .then()
      .statusCode(OK.getStatusCode())
      .body("$.size().toString()", Matchers.is("4"),
        "username.chars", Matchers.containsInAnyOrder("jdoe","admin", "mrsquare", "mrtriangle")
      );
  }

  @Test
  public void testCRUDUsers() {

    RestAssured.given().auth().oauth2(getAccessToken("admin"))
      .when()
      .body(new UserDto("mr","rectangle","mrrectangule@trikorasolutions.com",
        true,"mrrectangule"))
      .contentType("application/json")
      .post("/api/admin/trikorasolutions/users")
      .then()
      .statusCode(OK.getStatusCode())
      ;
  }


}
