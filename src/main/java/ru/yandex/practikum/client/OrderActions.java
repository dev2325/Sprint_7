package ru.yandex.practikum.client;

import io.restassured.response.Response;
import ru.yandex.practikum.dto.OrderRequest;
import ru.yandex.practikum.dto.TrackNumRequest;

import static io.restassured.RestAssured.given;
import static ru.yandex.practikum.config.Config.*;

public class OrderActions {

    public Response placeOrder(OrderRequest randomOrderRequest) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .and()
                .body(randomOrderRequest)
                .when()
                .post(ORDERS);
    }

    public Response cancelOrder(Integer trackNum) {
        TrackNumRequest trackIdObj = new TrackNumRequest(trackNum);
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .body(trackIdObj)
                .put(ORDERS_CANCEL);
    }

    public Response getOrderByTrackNum(Integer trackNum) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .queryParam("t", trackNum)
                .get(ORDERS_TRACK);
    }

    public Response acceptOrder(Integer courierId, Integer orderId) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .queryParam("courierId", courierId)
                .put(ORDERS_ACCEPT + "/" + orderId);
    }

    public Response getOrdersCount(Integer courierId) {
        Response response = given()
                .header("Content-type", "application/json")
                .baseUri(BASE_URL)
                .get(COURIER_ORDERS_COUNT, courierId);
        return response;
    }
}
