import io.restassured.response.Response;
import org.junit.Test;
import ru.yandex.practikum.client.CourierActions;
import ru.yandex.practikum.dto.CourierRequest;
import ru.yandex.practikum.dto.LoginRequest;
import ru.yandex.practikum.generator.CourierRequestGenerator;
import ru.yandex.practikum.generator.LoginRequestGenerator;
import ru.yandex.practikum.utils.Utils;

import java.io.File;


public class LoginCourierTest {

    CourierRequest randomCourierRequest = CourierRequestGenerator.getRandomCourierRequest();
    CourierActions courierActions = new CourierActions();

    @Test
    public void authCourierPositiveTest() {
        Response responseNewCourier = courierActions.create(randomCourierRequest); // регистрируем курьера и сохраняем ответ

        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest); // подготовим объект с данными для авторизации
        Response responseAuth = courierActions.login(loginRequest); // авторизуемся и сохраним ответ
        Utils.checkResponseReturnSuccessCode(responseAuth); // проверяем код ответа

        Integer courierId = courierActions.loginReturnId(loginRequest); // получим id курьера
        courierActions.delete(courierId); // удаляем созданого курьера по его id
    }

    @Test
    public void tryAuthCourierWithoutLoginReturnBadRequest() {
        // тут значения данных ни на что не влияют, важна их структура. поэтому использован файл
        File json = new File("src/test/resources/courierData_Password.json");
        Response responseAuth = courierActions.loginWithFile(json); // попытка авторизации без логина, сохраним ответ
        Utils.checkResponseReturnBadRequestCode(responseAuth); // проверяем код ответа
        Utils.checkAuthCourierInsufficientDataReturnBadRequestMessage(responseAuth); // проверяем тело ответа
    }

    // этот тест падает т.к. по документации должна прийти ошибка 409, а приходит сообщение "Service unavailable"
    @Test
    public void tryAuthCourierWithoutPasswordReturnBadRequest() {
        File json = new File("src/test/resources/courierData_Login.json");
        Response responseAuth = courierActions.loginWithFile(json); // попытка авторизации без пароля, сохраним ответ
        Utils.checkResponseReturnBadRequestCode(responseAuth); // проверяем код ответа
        Utils.checkAuthCourierInsufficientDataReturnBadRequestMessage(responseAuth); // проверяем тело ответа
    }

    @Test
    public void tryAuthCourierIncorrectLoginThenNotFound() {
        courierActions.create(randomCourierRequest); // регистрируем курьера
        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest); // подготовим объект с данными для авторизации
        Integer courierId = courierActions.loginReturnId(loginRequest); // проверяем что c этими данными заходит и сохраним id курьера

        LoginRequest loginRequestChanged = LoginRequestGenerator.changeLoginFrom(loginRequest); // меняем текущий логин курьера
        Response responseAuth = courierActions.login(loginRequestChanged); // попытка авторизации c измененным логином

        Utils.checkResponseReturnNotFoundCode(responseAuth); // проверяем код ответа
        Utils.checkAuthCourierIncorrectDataReturnNotFoundMessage(responseAuth); // проверяем тело ответа
        courierActions.delete(courierId); // удаляем созданого курьера по его id
    }

    @Test
    public void tryAuthCourierIncorrectPasswordThenNotFound() {
        courierActions.create(randomCourierRequest); // регистрируем курьера
        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest); // подготовим объект с данными для авторизации
        Integer courierId = courierActions.loginReturnId(loginRequest); // проверяем что c этими данными заходит и сохраним id курьера

        LoginRequest loginRequestChanged = LoginRequestGenerator.changePasswordFrom(loginRequest); // меняем текущий пароль курьера
        Response responseAuth = courierActions.login(loginRequestChanged); // попытка авторизации c измененным паролем

        Utils.checkResponseReturnNotFoundCode(responseAuth); // проверяем код ответа
        Utils.checkAuthCourierIncorrectDataReturnNotFoundMessage(responseAuth); // проверяем тело ответа
        courierActions.delete(courierId); // удаляем созданого курьера по его id
    }

    @Test
    public void tryAuthNonexistentCourierThenNotFound() {
        courierActions.create(randomCourierRequest); // регистрируем курьера
        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest); // подготовим объект с данными для авторизации
        Integer courierId = courierActions.loginReturnId(loginRequest);// проверяем что c этими данными заходит и сохраним id курьера
        courierActions.delete(courierId); // удаляем созданого курьера

        Response responseAuth = courierActions.login(loginRequest); // попытка авторизации c теми же данными
        Utils.checkResponseReturnNotFoundCode(responseAuth); // проверяем код ответа
        Utils.checkAuthCourierIncorrectDataReturnNotFoundMessage(responseAuth); // проверяем тело ответа
    }
}
