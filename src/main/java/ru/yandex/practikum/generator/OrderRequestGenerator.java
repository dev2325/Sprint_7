package ru.yandex.practikum.generator;

import org.apache.commons.lang3.RandomStringUtils;
import ru.yandex.practikum.dto.OrderRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static ru.yandex.practikum.config.Config.getBaseUri;

public class OrderRequestGenerator {

    public static OrderRequest getRandomOrderRequest() {
        OrderRequest orderRequest = new OrderRequest();
        String[] colors = new String[]{};

        orderRequest.setFirstName(RandomStringUtils.randomAlphabetic(10));
        orderRequest.setLastName(RandomStringUtils.randomAlphabetic(10));
        orderRequest.setAddress(RandomStringUtils.randomAlphabetic(10));
        orderRequest.setMetroStation(getMetroStation());
        orderRequest.setPhone("+7900" + new Random().nextInt(10_000_000));
        orderRequest.setRentTime(1 + new Random().nextInt(7));
        orderRequest.setDeliveryDate(getRandomDate());
        orderRequest.setComment(RandomStringUtils.randomAlphabetic(10));
        orderRequest.setColor(colors);

        return orderRequest;
    }

    public static String getRandomDate() {
        Random rand = new Random();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.set(2022, 10, 16); // стартовая дата
        long start = cal.getTimeInMillis();
        cal.set(2022, 11, 28); // конечная дата
        long end = cal.getTimeInMillis();
        Date d = new Date(start + (long) (rand.nextDouble() * (end - start)));
        return format.format(d);
    }

    public static String getMetroStation() {
        // получение списка метро и сохранение "number" первой станции из массива данных
        String metroNumber = given()
                .baseUri(getBaseUri())
                .get("/api/v1/stations/search?s=")
                .then().extract().body().path("[0].number");
        return metroNumber;
    }
}
