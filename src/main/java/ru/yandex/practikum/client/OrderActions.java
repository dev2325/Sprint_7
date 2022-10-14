package ru.yandex.practikum.client;

import io.restassured.response.Response;
import ru.yandex.practikum.dto.OrderRequest;
import ru.yandex.practikum.dto.TrackNumRequest;

import static io.restassured.RestAssured.given;
import static ru.yandex.practikum.config.Config.getBaseUri;

public class OrderActions {

    public Response placeOrder(OrderRequest randomOrderRequest) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .and()
                .body(randomOrderRequest)
                .when()
                .post("/api/v1/orders");
    }

    public Response cancelOrder(Integer trackNum) {
        TrackNumRequest trackIdObj = new TrackNumRequest(trackNum);
        return given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .body(trackIdObj)
                .put("/api/v1/orders/cancel/");
    }

    public Response getOrderByTrackNum(Integer trackNum) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .get("/api/v1/orders/track?t={trackNum}", trackNum);
    }

    public Response acceptOrder(Integer courierId, Integer orderId) {
        return given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .queryParam("courierId", courierId)
                .put("/api/v1/orders/accept/{orderId}", orderId);
    }
}
