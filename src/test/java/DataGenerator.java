import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;


import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private DataGenerator() {
    }

    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static Faker faker = new Faker(new Locale("en"));

    public static String getLogin() {
        String login = faker.lorem().characters(8);
        return login;
    }

    public static String getPassword() {
        String password = faker.lorem().characters(10);
        return password;
    }

    @Value
    public static class RegistrationData {
        String login;
        String password;
        String status;
    }

    public static RegistrationData getUser(String status) {

        RegistrationData user;
        user = new RegistrationData(getLogin(), getPassword(), status);
        return user;
    }

    private static void sendPostRequest(RegistrationData user) {

        given()
                .spec(requestSpec)
                .body(user)

                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);

    }

    public static RegistrationData getRegisteredUser(String status) {
        RegistrationData registeredUser = getUser(status);
        sendPostRequest(registeredUser);
        return registeredUser;
    }

}
