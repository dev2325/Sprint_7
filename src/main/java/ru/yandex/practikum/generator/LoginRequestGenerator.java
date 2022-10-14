package ru.yandex.practikum.generator;

import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.practikum.dto.CourierRequest;
import ru.yandex.practikum.dto.LoginRequest;

public class LoginRequestGenerator {

    public static LoginRequest prepareFrom(CourierRequest courierRequest) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setLogin(courierRequest.getLogin());
        loginRequest.setPassword(courierRequest.getPassword());
        return loginRequest;
    }

    public static LoginRequest changeLoginFrom(LoginRequest loginRequest) {
        loginRequest.setLogin(RandomStringUtils.randomAlphabetic(10));
        loginRequest.setPassword(loginRequest.getPassword());
        return loginRequest;
    }

    public static LoginRequest changePasswordFrom(LoginRequest loginRequest) {
        loginRequest.setLogin(loginRequest.getLogin());
        loginRequest.setPassword(RandomStringUtils.randomAlphabetic(10));
        return loginRequest;
    }
}
