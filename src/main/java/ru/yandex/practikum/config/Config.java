package ru.yandex.practikum.config;

public class Config {

    public static final String BASE_URL = "https://qa-scooter.praktikum-services.ru";

    public static final String CONFLICT_MESSAGE = "Этот логин уже используется. Попробуйте другой.";
    public static final String BAD_REQUEST_MESSAGE = "Недостаточно данных для создания учетной записи";
    public static final String AUTH_BAD_REQUEST_MESSAGE = "Недостаточно данных для входа";
    public static final String NOT_FOUND_MESSAGE = "Учетная запись не найдена";

    public static final String COLOR_BLACK = "BLACK";
    public static final String COLOR_GREY = "GREY";

    public static final String COURIER = "/api/v1/courier";
    public static final String COURIER_LOGIN = "/api/v1/courier/login";
    public static final String COURIER_ORDERS_COUNT = "/api/v1/courier/{courierId}/ordersCount";

    public static final String ORDERS = "/api/v1/orders";
    public static final String ORDERS_CANCEL = "/api/v1/orders/cancel";
    public static final String ORDERS_ACCEPT = "/api/v1/orders/accept";
    public static final String ORDERS_TRACK = "/api/v1/orders/track";

    public static final String STATIONS_SEARCH = "/api/v1/stations/search";
}
