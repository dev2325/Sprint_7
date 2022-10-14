package ru.yandex.practikum.config;

public class Config {

    public static final String BASE_URI = "https://qa-scooter.praktikum-services.ru";

    public static final String CONFLICT_MESSAGE = "Этот логин уже используется. Попробуйте другой.";
    public static final String BAD_REQUEST_MESSAGE = "Недостаточно данных для создания учетной записи";
    public static final String AUTH_BAD_REQUEST_MESSAGE = "Недостаточно данных для входа";
    public static final String NOT_FOUND_MESSAGE = "Учетная запись не найдена";

    public static final String COLOR_BLACK = "BLACK";
    public static final String COLOR_GREY = "GREY";

    public static String getBaseUri() {
        return BASE_URI;
    }
}
