package ru.yandex.practikum.client;

import io.restassured.response.Response;
import ru.yandex.practikum.dto.LoginRequest;

import java.io.File;

import static io.restassured.RestAssured.given;
import static ru.yandex.practikum.config.Config.getBaseUri;

public class CourierActions {

    public Response create(Object randomCourierRequest) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .and()
                .body(randomCourierRequest)
                .when()
                .post("/api/v1/courier");
    }

    public Response login(LoginRequest loginRequest) {
        Response response = given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .and()
                .body(loginRequest)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }

    public Integer loginReturnId(LoginRequest loginRequest) {
        Integer courierId = login(loginRequest).then().extract().body().path("id");
        return courierId;
    }

    public Response loginWithFile(File file) {
        Response response = given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .and()
                .body(file)
                .when()
                .post("/api/v1/courier/login");
        return response;
    }

    public boolean delete(Integer courierId) {
        boolean isDeleted = given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .delete("/api/v1/courier/{id}", courierId)
                .then().extract().body().path("ok");
//        System.out.println(courierId + " был удален? " + isDeleted);
        return isDeleted;
    }

    public Response getOrdersCount(Integer courierId) {
        Response response = given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .get("/api/v1/courier/{courierId}/ordersCount", courierId);
        return response;
    }
}
