import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import ru.yandex.practikum.client.CourierActions;
import ru.yandex.practikum.dto.CourierRequest;
import ru.yandex.practikum.dto.LoginRequest;
import ru.yandex.practikum.generator.CourierRequestGenerator;
import ru.yandex.practikum.generator.LoginRequestGenerator;
import ru.yandex.practikum.utils.ResponseCheck;

import java.io.File;


public class LoginCourierTest {

    CourierRequest randomCourierRequest = CourierRequestGenerator.getRandomCourierRequest();
    CourierActions courierActions = new CourierActions();
    private Integer courierId;

    @Test
    @DisplayName("Login courier positive test")
    public void authCourierPositiveTest() {
        courierActions.createCourier(randomCourierRequest); // регистрируем курьера
        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest); // подготовим объект с данными для авторизации
        Response responseAuth = courierActions.login(loginRequest); // авторизуемся и сохраним ответ
        ResponseCheck.checkResponseReturnSuccessCode(responseAuth); // проверяем код ответа

        courierId = courierActions.loginReturnId(loginRequest); // сохраним id курьера
    }

    @Test
    @DisplayName("Try to login courier without login field causes 400 error")
    public void tryAuthCourierWithoutLoginReturnBadRequest() {
        // тут значения данных ни на что не влияют, важна их структура. поэтому использован файл
        File json = new File("src/test/resources/courierData_Password.json");
        Response responseAuth = courierActions.loginWithFile(json); // попытка авторизации без логина, сохраним ответ
        ResponseCheck.checkResponseReturnBadRequestCode(responseAuth); // проверяем код ответа
        ResponseCheck.checkAuthCourierInsufficientDataReturnBadRequestMessage(responseAuth); // проверяем тело ответа
    }

    // этот тест падает т.к. по документации должна прийти ошибка 400, а приходит 504 "Service unavailable"
    @Test
    @DisplayName("Try to login courier without password field causes 400 error")
    public void tryAuthCourierWithoutPasswordReturnBadRequest() {
        File json = new File("src/test/resources/courierData_Login.json");
        Response responseAuth = courierActions.loginWithFile(json); // попытка авторизации без пароля, сохраним ответ
        ResponseCheck.checkResponseReturnBadRequestCode(responseAuth); // проверяем код ответа
        ResponseCheck.checkAuthCourierInsufficientDataReturnBadRequestMessage(responseAuth); // проверяем тело ответа
    }

    @Test
    @DisplayName("Try to login courier with incorrect login causes 404 error")
    public void tryAuthCourierIncorrectLoginThenNotFound() {
        courierActions.createCourier(randomCourierRequest); // регистрируем курьера
        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest); // подготовим объект с данными для авторизации
        courierId = courierActions.loginReturnId(loginRequest); // проверяем что c этими данными заходит и сохраним id курьера

        LoginRequest loginRequestChanged = LoginRequestGenerator.changeLoginFrom(loginRequest); // меняем текущий логин курьера
        Response responseAuth = courierActions.login(loginRequestChanged); // попытка авторизации c измененным логином

        ResponseCheck.checkResponseReturnNotFoundCode(responseAuth); // проверяем код ответа
        ResponseCheck.checkAuthCourierIncorrectDataReturnNotFoundMessage(responseAuth); // проверяем тело ответа
    }

    @Test
    @DisplayName("Try to login courier with incorrect password causes 404 error")
    public void tryAuthCourierIncorrectPasswordThenNotFound() {
        courierActions.createCourier(randomCourierRequest); // регистрируем курьера
        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest); // подготовим объект с данными для авторизации
        courierId = courierActions.loginReturnId(loginRequest); // проверяем что c этими данными заходит и сохраним id курьера

        LoginRequest loginRequestChanged = LoginRequestGenerator.changePasswordFrom(loginRequest); // меняем текущий пароль курьера
        Response responseAuth = courierActions.login(loginRequestChanged); // попытка авторизации c измененным паролем

        ResponseCheck.checkResponseReturnNotFoundCode(responseAuth); // проверяем код ответа
        ResponseCheck.checkAuthCourierIncorrectDataReturnNotFoundMessage(responseAuth); // проверяем тело ответа
    }

    @Test
    @DisplayName("Try to login non-existent courier causes 404 error")
    public void tryAuthNonexistentCourierThenNotFound() {
        courierActions.createCourier(randomCourierRequest); // регистрируем курьера
        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest); // подготовим объект с данными для авторизации
        courierId = courierActions.loginReturnId(loginRequest);// проверяем что c этими данными заходит и сохраним id

        Boolean isDeleted = courierActions.deleteCourier(courierId); // удаляем созданого курьера
        courierId = isDeleted ? null : courierId; // если успешно удален, зануляем courierId

        Response responseAuth = courierActions.login(loginRequest); // попытка авторизации c теми же данными
        ResponseCheck.checkResponseReturnNotFoundCode(responseAuth); // проверяем код ответа
        ResponseCheck.checkAuthCourierIncorrectDataReturnNotFoundMessage(responseAuth); // проверяем тело ответа
    }

    @After
    public void cleanData() {
        if (courierId != null) {
            courierActions.deleteCourier(courierId);
        }
    }
}
