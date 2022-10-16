import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.practikum.client.OrderActions;
import ru.yandex.practikum.dto.OrderRequest;
import ru.yandex.practikum.generator.OrderRequestGenerator;
import ru.yandex.practikum.utils.ResponseCheck;

import static ru.yandex.practikum.config.Config.COLOR_BLACK;
import static ru.yandex.practikum.config.Config.COLOR_GREY;


@RunWith(Parameterized.class)
public class OrderParamTest {


    OrderRequest randomOrderRequest = OrderRequestGenerator.getRandomOrderRequest();
    OrderActions orderActions = new OrderActions();

    private final String[] colors;

    public OrderParamTest(String[] colors) {
        this.colors = colors;
    }

    // тестовые данные
    @Parameterized.Parameters
    public static Object[][] getCredentials() {
        return new Object[][]{
                {new String[]{COLOR_BLACK}},
                {new String[]{COLOR_GREY}},
                {new String[]{COLOR_BLACK, COLOR_GREY}},
                {new String[]{}},
        };
    }

    @Test
    @DisplayName("Place new order with different colors positive test")
    public void placeOrderDifferentColorsPositiveTest() {
        randomOrderRequest.setColor(colors); // установим объекту заказа массив цветов
        Response responseNewOrder = orderActions.placeOrder(randomOrderRequest); // размещаем заказ и сохраняем ответ
        ResponseCheck.checkResponseReturnSuccessCreatedCode(responseNewOrder); // проверяем код ответа
        ResponseCheck.checkPlaceNewOrderReturnTrackNotNull(responseNewOrder); // проверяем тело ответа
    }
}
