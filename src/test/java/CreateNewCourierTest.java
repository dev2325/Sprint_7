import io.restassured.response.Response;
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

    @Test
    public void createCourierPositiveTest() {
        Response responseNewCourier = courierActions.create(randomCourierRequest); // регистрируем курьера и сохраняем ответ
        Utils.checkResponseReturnSuccessCreatedCode(responseNewCourier); // проверяем код ответа
        Utils.checkResponseReturnOkTrueMessage(responseNewCourier); // проверяем тело ответа

        // подготовим объект избавляясь от firstName, оставим только данные для авторизации
        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest);

        Integer courierId = courierActions.loginReturnId(loginRequest); // проверяем что курьер действительно создался и сохраним его id
        courierActions.delete(courierId); // удаляем созданого курьера по его id
    }

    @Test
    public void tryCreateDuplicateCourierThenConflict() {
        Response responseNewCourier = courierActions.create(randomCourierRequest); // регистрируем курьера и сохраняем ответ
        Utils.checkResponseReturnSuccessCreatedCode(responseNewCourier); // проверяем код ответа
        Utils.checkResponseReturnOkTrueMessage(responseNewCourier); // проверяем тело ответа

        // подготовим объект для авторизации избавляясь от firstName
        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest);
        Integer courierId = courierActions.loginReturnId(loginRequest); // проверяем что курьер действительно создался и сохраним его id

        // пробуем регистрировать еще одного курьера с теми же учетными данными
        Response responseDuplicateCourier = courierActions.create(randomCourierRequest);
        Utils.checkResponseReturnConflictCode(responseDuplicateCourier); // проверяем код ответа
        Utils.checkCreateDuplicateReturnConflictMessage(responseDuplicateCourier); // проверяем тело ответа
        courierActions.delete(courierId); // удалим созданого курьера по его id
    }

    @Test
    public void tryCreateDuplicateLoginCourierThenConflict() {
        Response responseNewCourier = courierActions.create(randomCourierRequest); // регистрируем курьера и сохраняем ответ
        Utils.checkResponseReturnSuccessCreatedCode(responseNewCourier); // проверяем код ответа
        Utils.checkResponseReturnOkTrueMessage(responseNewCourier); // проверяем тело ответа

        // подготовим объект для авторизации избавляясь от firstName
        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest);
        Integer courierId = courierActions.loginReturnId(loginRequest); // проверяем что курьер действительно создался и сохраним его id

        // переделываем объект сменив пароль
        CourierRequest courierRequestChangedPassword = CourierRequestGenerator.changePasswordFrom(randomCourierRequest);

        Response response = courierActions.create(courierRequestChangedPassword); // пробуем регистрировать
        Utils.checkResponseReturnConflictCode(response); // проверяем код ответа
        Utils.checkCreateDuplicateReturnConflictMessage(response); // проверяем тело ответа
        courierActions.delete(courierId); // удаляем созданого курьера по его id
    }

    @Test
    public void tryCreateCourierWithoutLoginReturnBadRequest() {
        // тут значения данных ни на что не влияют, важна их структура. поэтому использован файл
        File json = new File("src/test/resources/courierData_PasswordFirstName.json");
        Response response = courierActions.create(json); // пробуем зарегистрировать курьера без поля login
        Utils.checkResponseReturnBadRequestCode(response); // проверяем код ответа
        Utils.checkCreateCourierInsufficientDataReturnBadRequestMessage(response); // проверяем тело ответа
    }

    @Test
    public void tryCreateCourierWithoutPasswordReturnBadRequest() {
        File json = new File("src/test/resources/courierData_LoginFirstName.json");
        Response response = courierActions.create(json); // пробуем зарегистрировать курьера без поля password
        Utils.checkResponseReturnBadRequestCode(response); // проверяем код ответа
        Utils.checkCreateCourierInsufficientDataReturnBadRequestMessage(response); // проверяем тело ответа
    }

    @Test
    public void createCourierWithoutFirstNamePositiveTest() {
        LoginRequest loginRequest = LoginRequestGenerator.prepareFrom(randomCourierRequest); // подготовим объект с данными для авторизации
        Response responseNewCourier = courierActions.create(loginRequest); // регистрируем курьера и сохраняем ответ
        Utils.checkResponseReturnSuccessCreatedCode(responseNewCourier); // проверяем код ответа
        Utils.checkResponseReturnOkTrueMessage(responseNewCourier); // проверяем тело ответа

        Integer courierId = courierActions.loginReturnId(loginRequest); // проверяем что курьер действительно создался и сохраним его id
        courierActions.delete(courierId); // удаляем созданого курьера по его id
    }

}
