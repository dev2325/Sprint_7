package ru.yandex.practikum.utils;

import io.restassured.response.Response;
import ru.yandex.practikum.config.Config;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class ResponseCheck {

    public static void checkResponseReturnSuccessCode(Response response) {
        response.then().assertThat().statusCode(SC_OK); // code 200
    }

    public static void checkResponseReturnSuccessCreatedCode(Response response) {
        response.then().assertThat().statusCode(SC_CREATED); // code 201
    }

    public static void checkResponseReturnBadRequestCode(Response response) {
        response.then().assertThat().statusCode(SC_BAD_REQUEST); // code 400
    }

    public static void checkResponseReturnNotFoundCode(Response response) {
        response.then().assertThat().statusCode(SC_NOT_FOUND); // code 404
    }

    public static void checkResponseReturnConflictCode(Response response) {
        response.then().assertThat().statusCode(SC_CONFLICT); // code 409
    }

    public static void checkResponseReturnOkTrueMessage(Response response) {
        response.then().assertThat().body("ok", equalTo(true));
    }

    public static void checkCreateDuplicateReturnConflictMessage(Response response) {
        response.then().assertThat().body("message", equalTo(Config.CONFLICT_MESSAGE));
    }

    public static void checkCreateCourierInsufficientDataReturnBadRequestMessage(Response response) {
        response.then().assertThat().body("message", equalTo(Config.BAD_REQUEST_MESSAGE));
    }

    public static void checkAuthCourierInsufficientDataReturnBadRequestMessage(Response response) {
        response.then().assertThat().body("message", equalTo(Config.AUTH_BAD_REQUEST_MESSAGE));
    }

    public static void checkAuthCourierIncorrectDataReturnNotFoundMessage(Response response) {
        response.then().assertThat().body("message", equalTo(Config.NOT_FOUND_MESSAGE));
    }

    public static void checkPlaceNewOrderReturnTrackNotNull(Response response) {
        response.then().assertThat().body("track", notNullValue());
    }
}
