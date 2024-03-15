import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import site.nomoreparties.stellarburgers.models.User;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static site.nomoreparties.stellarburgers.constants.ResponseMessage.*;

public class AuthTests extends BaseTest{

    @Test
    @DisplayName("Авторизация зарегистрированного юзера")
    public void testAuthRegistredUser() {
        regUser(newUser);
        User loginUser = new User(newUser.getEmail(), newUser.getPassword());
        Response authUser = authUser(loginUser);
        authUser
                .then()
                .statusCode(200)
                .assertThat().body("success", is(true))
                .assertThat().body("user.email", equalTo(newUser.getEmail()))
                .assertThat().body("user.name", equalTo(newUser.getName()))
                .assertThat().body("accessToken", notNullValue())
                .assertThat().body("refreshToken", notNullValue());

    }
    @Test
    @DisplayName("Авторизация с некорректынм email")
    public void testAuthWithIncorrectEmail() {
        regUser(newUser);
        User loginUser = new User(newUser.getEmail()+"fail", newUser.getPassword());
        Response authUser = authUser(loginUser);
        authUser
                .then()
                .statusCode(401)
                .assertThat().body("success", is(false))
                .assertThat().body("message", equalTo(UNAUTHORIZED));

    }
    @Test
    @DisplayName("Авторизация с некорректынм паролем")
    public void testAuthWithIncorrectPassword() {
        regUser(newUser);
        User loginUser = new User(newUser.getEmail(), newUser.getPassword()+"fail");
        Response authUser = authUser(loginUser);
        authUser
                .then()
                .statusCode(401)
                .assertThat().body("success", is(false))
                .assertThat().body("message", equalTo(UNAUTHORIZED));
    }
}
