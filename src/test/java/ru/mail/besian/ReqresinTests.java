package ru.mail.besian;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

public class ReqresinTests {
    @BeforeAll
    static void prepare() {
        RestAssured.baseURI = "https://reqres.in/";
    }

    @Test
    void loginSuccessful() {
        String data = "{\"email\": \"eve.holt@reqres.in\",\"password\": \"pistol\" }";
        RestAssured
                .given().log().all()
                .contentType(JSON)
                .body(data)
                .when()
                .post("api/login")
                .then().log().all()
                .body("token", is("QpwL5tke4Pnpja7X4"))
                .statusCode(200);

    }

    @Test
    void updateSuccessful() {
        String requestData = "{\"name\": \"morpheus\", \"job\": \"zion resident\" }";
        String responseData = "{ \"name\": \"morpheus\", \"job\": \"zion resident\", \"updatedAt\": \"2021-12-24T20:01:57.004Z\" }";
        String response = RestAssured
                .given().log().all()
                .contentType(JSON)
                .body(requestData)
                .when()
                .patch("api/users/2")
                .then().log().all()
                .statusCode(200)
                .extract().response().toString();
        assertThat(responseData.equals(response));

    }

    @Test
    void createSuccessful() {
        String job = "\"leader\"";
        String requestData = "{\"name\": \"morpheus\", \"job\":" + job + "}";
        String response = RestAssured
                .given().log().all()
                .contentType(JSON)
                .body(requestData)
                .when()
                .post("api/users")
                .then().log().all()
                .statusCode(201)
                .extract().response().path("job");
        assertThat(response.equals(job));

    }

    @Test
    void delayedResponse() {
        String responseData = "{ \"id\": 6, \"email\": \"tracey.ramos@reqres.in\", \"first_name\": \"Tracey\", \"last_name\": \"Ramos\", \"avatar\": \"https://reqres.in/img/faces/6-image.jpg\" }";
        ArrayList response = RestAssured
                .given().log().all()
                .contentType(JSON)
                .queryParams("delay", 3)
                .when()
                .get("api/users")
                .then().log().all()
                .statusCode(200)
                .extract().response().path("data");
        assertThat(response).hasSize(6);
        assertThat(response.get(5)).toString().equals(responseData);


    }

    @Test
    void delete() {
        RestAssured
                .given().log().all()
                .contentType(JSON)
                .when()
                .delete("api/users/2")
                .then().log().all()
                .statusCode(204);


    }
}
