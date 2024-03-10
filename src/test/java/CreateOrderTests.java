import io.restassured.response.Response;
import org.junit.Test;
import site.nomoreparties.stellarburgers.models.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.*;
import static site.nomoreparties.stellarburgers.constants.ResponseMessage.NO_ID_INGREDIENTS;

public class CreateOrderTests extends BaseTest {

    @Test
    public void testCreateOrderWithAuth() {
        Response regUser = regUser(newUser);
        String token = regUser.jsonPath().getString("accessToken");
        List<String> ingredients = getIngredients()
                .getBody()
                .path("data._id");
        Order order = new Order(ingredients);
        Response createOrder = createOrder(token, order);
        createOrder
                .then()
                .statusCode(200)
                .assertThat().body("success", is(true))
                .assertThat().body("name", notNullValue())
                .assertThat().body("order.number", notNullValue());
    }

    @Test
    public void testCreateOrderWithoutAuth() {
        List<String> ingredients = getIngredients()
                .getBody()
                .path("data._id");
        Order order = new Order(ingredients);
        Response createOrder = createOrder("", order);
        createOrder
                .then()
                .statusCode(200)
                .assertThat().body("success", is(true))
                .assertThat().body("name", notNullValue())
                .assertThat().body("order.number", notNullValue());
    }

    @Test
    public void testCreateOrderWithoutIngredients() {
        Response regUser = regUser(newUser);
        String token = regUser.jsonPath().getString("accessToken");
        List<String> ingredients = new ArrayList<>();
        Order order = new Order(ingredients);
        Response createOrder = createOrder(token, order);
        createOrder
                .then()
                .statusCode(400)
                .assertThat().body("success", is(false))
                .assertThat().body("message", equalTo(NO_ID_INGREDIENTS));
    }

    @Test
    public void testCreateOrderWithIncorrectIngredients() {
        Response regUser = regUser(newUser);
        String token = regUser.jsonPath().getString("accessToken");
        List<String> ingredients = List.of("Ingredient"+ new Random().nextInt(1000));
        Order order = new Order(ingredients);
        Response createOrder = createOrder(token, order);
        createOrder
                .then()
                .statusCode(500);
    }
}
