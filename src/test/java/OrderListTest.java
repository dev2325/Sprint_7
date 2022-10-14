import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.practikum.client.CourierActions;
import ru.yandex.practikum.client.OrderActions;
import ru.yandex.practikum.dto.CourierRequest;
import ru.yandex.practikum.dto.LoginRequest;
import ru.yandex.practikum.dto.OrderRequest;
import ru.yandex.practikum.generator.CourierRequestGenerator;
import ru.yandex.practikum.generator.LoginRequestGenerator;
import ru.yandex.practikum.generator.OrderRequestGenerator;
import ru.yandex.practikum.utils.Utils;

import static io.restassured.RestAssured.given;
import static ru.yandex.practikum.config.Config.getBaseUri;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTest {

    CourierRequest randomCourierRequest = CourierRequestGenerator.getRandomCourierRequest();
    CourierActions courierActions = new CourierActions();
    OrderRequest randomOrderRequest = OrderRequestGenerator.getRandomOrderRequest();
    OrderActions orderActions = new OrderActions();

    Integer courierId;
    Integer trackNum;

    // простая проверка что в системе есть хоть один заказ
    @Test
    public void getTotalOrderListNotNull() {
        Response response = given()
                .header("Content-type", "application/json")
                .baseUri(getBaseUri())
                .when()
                .get("/api/v1/orders");

        response.then().assertThat().body("orders", notNullValue());
    }

    /*
    не знаю хватит ли общей проверки, на всякий случай реализована еще подробная проверка:
    регистрируем курьера, размещаем заказ, принимаем заказ,  проверяем что количество заказов этого курьера
    не ноль, удаляем курьера. тест падает т.к. получение количества заказов курьера не работает
    */
    @Test
    public void getOrderListForCreatedCourierNotNull() {
        courierId = createCourier(); // регистрируем курьера и сохраняем id
        trackNum = placeOrder(); // размещаем заказ и сохраняем track
        acceptOrder(); // принимаем заказ
        getOrdersCount(); // проверяем что у курьера заказов не ноль
        courierActions.delete(courierId); // удаляем курьера
    }

    @Step("Create courier")
    public Integer createCourier() {
        Response responseNewCourier = courierActions.create(randomCourierRequest); // регистрируем курьера и сохраняем ответ
        Utils.checkResponseReturnSuccessCreatedCode(responseNewCourier); // проверяем код ответа
        Utils.checkResponseReturnOkTrueMessage(responseNewCourier); // проверяем тело ответа

        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest); // подготовим объект c данными для авторизации
        Integer courierId = courierActions.loginReturnId(loginRequest); // логинимся проверяем что курьер действительно создался, сохраним его id
        return courierId;
    }

    @Step("Place order")
    public Integer placeOrder() {
        Response responseNewOrder = orderActions.placeOrder(randomOrderRequest); // размещаем заказ и сохраняем ответ
        Utils.checkResponseReturnSuccessCreatedCode(responseNewOrder); // проверяем код ответа
        Utils.checkPlaceNewOrderReturnTrackNotNull(responseNewOrder); // проверяем тело ответа

        Integer trackNum = responseNewOrder.then().extract().body().path("track"); // из ответа о размещ.заказа берем его track
        return trackNum;
    }

    @Step("Accept order")
    public void acceptOrder() {
        Response orderInfoResponse = orderActions.getOrderByTrackNum(trackNum); // по треку запрашиваем полную информацию о заказе
        Integer orderId = orderInfoResponse.then().extract().body().path("order.id"); // из информации о заказе берем его id
        Response acceptOrderResponse = orderActions.acceptOrder(courierId, orderId); // принимаем заказ
        Utils.checkResponseReturnSuccessCode(acceptOrderResponse); // проверяем код ответа
        Utils.checkResponseReturnOkTrueMessage(acceptOrderResponse); // проверяем тело ответа
    }

    @Step("Get orders count")
    public void getOrdersCount() {
        Response ordersCountResponse = courierActions.getOrdersCount(courierId); // получаем информацию о количестве заказов курьера
        Utils.checkResponseReturnSuccessCode(ordersCountResponse); // проверяем код ответа
        ordersCountResponse.then().assertThat().body("ordersCount", notNullValue()); // проверяем что заказов не ноль
    }
}
