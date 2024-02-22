import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class LogInTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldLoginRegisteredActiveUser() {
        var registeredUser = DataGenerator.getRegisteredUser("active");

        $(By.cssSelector("[data-test-id='login'] input.input__control")).setValue(registeredUser.getLogin());
        $(By.cssSelector("[data-test-id='password'] input.input__control")).setValue(registeredUser.getPassword());
        $$(By.cssSelector("button")).first().click();
        $(By.cssSelector("h2")).shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Личный кабинет"));

    }

    @Test
    void shouldFailForNotRegisteredUser() {
        var notRegisteredUser = DataGenerator.getUser("active");

        $(By.cssSelector("[data-test-id='login'] input.input__control")).setValue(notRegisteredUser.getLogin());
        $(By.cssSelector("[data-test-id='password'] input.input__control")).setValue(notRegisteredUser.getPassword());
        $$(By.cssSelector("button")).first().click();
        $(By.cssSelector("[data-test-id='error-notification'] .notification__content")).shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Ошибка! " + "Неверно указан логин или пароль"));

    }

    @Test
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = DataGenerator.getRegisteredUser("blocked");

        $(By.cssSelector("[data-test-id='login'] input.input__control")).setValue(blockedUser.getLogin());
        $(By.cssSelector("[data-test-id='password'] input.input__control")).setValue(blockedUser.getPassword());
        $$(By.cssSelector("button")).first().click();
        $(By.cssSelector("[data-test-id='error-notification'] .notification__content")).shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Ошибка! " + "Пользователь заблокирован"));

    }

    @Test
    void shouldFailWithWrongLogin() {
        var registeredUser = DataGenerator.getRegisteredUser("active");
        var wrongLogin = DataGenerator.getLogin();

        $(By.cssSelector("[data-test-id='login'] input.input__control")).setValue(wrongLogin);
        $(By.cssSelector("[data-test-id='password'] input.input__control")).setValue(registeredUser.getPassword());
        $$(By.cssSelector("button")).first().click();
        $(By.cssSelector("[data-test-id='error-notification'] .notification__content")).shouldBe(visible).shouldHave(text("Ошибка! " + "Неверно указан логин или пароль"));

    }

    @Test
    void shouldFailWithWrongPassword() {
        var registeredUser = DataGenerator.getRegisteredUser("active");
        var wrongPassword = DataGenerator.getPassword();

        $(By.cssSelector("[data-test-id='login'] input.input__control")).setValue(registeredUser.getLogin());
        $(By.cssSelector("[data-test-id='password'] input.input__control")).setValue(wrongPassword);
        $$(By.cssSelector("button")).first().click();
        $(By.cssSelector("[data-test-id='error-notification'] .notification__content")).shouldBe(visible).shouldHave(text("Ошибка! " + "Неверно указан логин или пароль"));

    }
}
