import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static site.nomoreparties.stellarburgers.constants.ResponseMessage.REQ_FIELDS_NOT_FILLED;
import static site.nomoreparties.stellarburgers.constants.ResponseMessage.USER_EXIST;

public class RegUserTests extends BaseTest {

    @Test
    public void testRegNewUser() {
        Response regUser = regUser(newUser);
        regUser
                .then()
                .statusCode(200)
                .assertThat().body("success", is(true))
                .assertThat().body("user.email", equalTo(newUser.getEmail()))
                .assertThat().body("user.name", equalTo(newUser.getName()))
                .assertThat().body("accessToken", notNullValue())
                .assertThat().body("refreshToken", notNullValue());
    }

    @Test
    public void testRegisterAlreadyRegistredUser() {
        regUser(newUser);
        Response regAlreadyRegistredUser = regUser(newUser);
        regAlreadyRegistredUser
                .then()
                .statusCode(403)
                .assertThat().body("success", is(false))
                .assertThat().body("message", equalTo(USER_EXIST));
    }

    @Test
    public void testRegUserWithoutName() {
        Response regUser = regUser(UserNoName);
        regUser
                .then()
                .statusCode(403)
                .assertThat().body("success", is(false))
                .assertThat().body("message", equalTo(REQ_FIELDS_NOT_FILLED));
    }

    @Test
    public void testRegUserWithoutEmail() {
        Response regUser = regUser(UserNoEmail);
        regUser
                .then()
                .statusCode(403)
                .assertThat().body("success", is(false))
                .assertThat().body("message", equalTo(REQ_FIELDS_NOT_FILLED));
    }

    @Test
    public void testRegUserWithoutPassword() {
        Response regUser = regUser(UserNoPassword);
        regUser
                .then()
                .statusCode(403)
                .assertThat().body("success", is(false))
                .assertThat().body("message", equalTo(REQ_FIELDS_NOT_FILLED));
    }
}
