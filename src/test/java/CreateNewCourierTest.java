import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import ru.yandex.practikum.client.CourierActions;
import ru.yandex.practikum.dto.*;
import ru.yandex.practikum.generator.*;
import ru.yandex.practikum.utils.*;

import java.io.File;


public class CreateNewCourierTest {

    // создали объект pojo и записали туда объект с рандомными данными
    CourierRequest randomCourierRequest = CourierRequestGenerator.getRandomCourierRequest();

    CourierActions courierActions = new CourierActions();
    private Integer courierId;

    @Test
    @DisplayName("Create new courier positive test")
    public void createCourierPositiveTest() {
        Response responseNewCourier = courierActions.createCourier(randomCourierRequest); // регистрируем курьера и сохраняем ответ
        ResponseCheck.checkResponseReturnSuccessCreatedCode(responseNewCourier); // проверяем код ответа
        ResponseCheck.checkResponseReturnOkTrueMessage(responseNewCourier); // проверяем тело ответа

        // подготовим объект избавляясь от firstName, оставим только данные для авторизации
        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest);

        courierId = courierActions.loginReturnId(loginRequest); // проверяем что курьер действительно создался и сохраним его id
    }

    @Test
    @DisplayName("Try create duplicate courier causes 409 error")
    public void tryCreateDuplicateCourierThenConflict() {
        Response responseNewCourier = courierActions.createCourier(randomCourierRequest); // регистрируем курьера и сохраняем ответ
        ResponseCheck.checkResponseReturnSuccessCreatedCode(responseNewCourier); // проверяем код ответа
        ResponseCheck.checkResponseReturnOkTrueMessage(responseNewCourier); // проверяем тело ответа

        // подготовим объект для авторизации избавляясь от firstName
        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest);
        courierId = courierActions.loginReturnId(loginRequest); // проверяем что курьер действительно создался и сохраним его id

        // пробуем регистрировать еще одного курьера с теми же учетными данными
        Response responseDuplicateCourier = courierActions.createCourier(randomCourierRequest);
        ResponseCheck.checkResponseReturnConflictCode(responseDuplicateCourier); // проверяем код ответа
        ResponseCheck.checkCreateDuplicateReturnConflictMessage(responseDuplicateCourier); // проверяем тело ответа
    }

    @Test
    @DisplayName("Try to create new courier with duplicate login causes 409 error")
    public void tryCreateDuplicateLoginCourierThenConflict() {
        Response responseNewCourier = courierActions.createCourier(randomCourierRequest); // регистрируем курьера и сохраняем ответ
        ResponseCheck.checkResponseReturnSuccessCreatedCode(responseNewCourier); // проверяем код ответа
        ResponseCheck.checkResponseReturnOkTrueMessage(responseNewCourier); // проверяем тело ответа

        // подготовим объект для авторизации избавляясь от firstName
        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest);
        courierId = courierActions.loginReturnId(loginRequest); // проверяем что курьер действительно создался и сохраним его id

        // переделываем объект сменив пароль
        CourierRequest courierRequestChangedPassword = CourierRequestGenerator.changePasswordFrom(randomCourierRequest);

        Response response = courierActions.createCourier(courierRequestChangedPassword); // пробуем регистрировать
        ResponseCheck.checkResponseReturnConflictCode(response); // проверяем код ответа
        ResponseCheck.checkCreateDuplicateReturnConflictMessage(response); // проверяем тело ответа
    }

    @Test
    @DisplayName("Try to create new courier without login field causes 400 error")
    public void tryCreateCourierWithoutLoginReturnBadRequest() {
        // тут значения данных ни на что не влияют, важна их структура. поэтому использован файл
        File json = new File("src/test/resources/courierData_PasswordFirstName.json");
        Response response = courierActions.createCourier(json); // пробуем зарегистрировать курьера без поля login
        ResponseCheck.checkResponseReturnBadRequestCode(response); // проверяем код ответа
        ResponseCheck.checkCreateCourierInsufficientDataReturnBadRequestMessage(response); // проверяем тело ответа
    }

    @Test
    @DisplayName("Try to create new courier without password field causes 400 error")
    public void tryCreateCourierWithoutPasswordReturnBadRequest() {
        File json = new File("src/test/resources/courierData_LoginFirstName.json");
        Response response = courierActions.createCourier(json); // пробуем зарегистрировать курьера без поля password
        ResponseCheck.checkResponseReturnBadRequestCode(response); // проверяем код ответа
        ResponseCheck.checkCreateCourierInsufficientDataReturnBadRequestMessage(response); // проверяем тело ответа
    }

    @Test
    @DisplayName("Create new courier without first name field positive test")
    public void createCourierWithoutFirstNamePositiveTest() {
        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest); // подготовим объект с данными для авторизации
        Response responseNewCourier = courierActions.createCourier(loginRequest); // регистрируем курьера и сохраняем ответ
        ResponseCheck.checkResponseReturnSuccessCreatedCode(responseNewCourier); // проверяем код ответа
        ResponseCheck.checkResponseReturnOkTrueMessage(responseNewCourier); // проверяем тело ответа
        courierId = courierActions.loginReturnId(loginRequest); // проверяем что курьер действительно создался и сохраним его id
    }

    @After
    public void cleanData() {
        if (courierId != null) {
            courierActions.deleteCourier(courierId);
        }
    }
}
