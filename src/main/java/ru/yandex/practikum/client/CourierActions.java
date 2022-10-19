package ru.yandex.practikum.client;

import io.restassured.response.Response;
import ru.yandex.practikum.dto.LoginRequest;

import java.io.File;

import static io.restassured.RestAssured.given;
import static ru.yandex.practikum.config.Config.*;

public class CourierActions {

    public Response createCourier(Object randomCourierRequest) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .and()
                .body(randomCourierRequest)
                .when()
                .post(COURIER);
    }

    public Response login(LoginRequest loginRequest) {
        Response response = given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .and()
                .body(loginRequest)
                .when()
                .post(COURIER_LOGIN);
        return response;
    }

    public Integer loginReturnId(LoginRequest loginRequest) {
        Integer courierId = login(loginRequest).then().extract().body().path("id");
        return courierId;
    }

    public Response loginWithFile(File file) {
        Response response = given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .and()
                .body(file)
                .when()
                .post(COURIER_LOGIN);
        return response;
    }

    public boolean deleteCourier(Integer courierId) {
        boolean isDeleted = given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .delete(COURIER + "/" + courierId)
                .then().extract().body().path("ok");
//        System.out.println(courierId + " был удален? " + isDeleted);
        return isDeleted;
    }
}
