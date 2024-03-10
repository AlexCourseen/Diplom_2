import io.restassured.response.Response;
import org.junit.Test;
import site.nomoreparties.stellarburgers.models.Order;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static site.nomoreparties.stellarburgers.constants.ResponseMessage.NEED_AUTH;

public class GerOrderTests extends BaseTest{

    @Test
    public void testGetOrdersOfAuthUser() {
        Response regUser = regUser(newUser);
        String token = regUser.jsonPath().getString("accessToken");
        List<String> ingredients = getIngredients()
                .getBody()
                .path("data._id");
        Order order = new Order(ingredients);
        Response createOrder = createOrder(token, order);
        int orderNumber = createOrder
                .getBody()
                .path("order.number");
        String orderId = createOrder
                .getBody()
                .path("order._id");
        Response getUsersOrders = getUsersOrders(token);
        getUsersOrders
                .then()
                .statusCode(200)
                .assertThat().body("success", is(true))
                .assertThat().body("orders.size()", equalTo(1))
                .assertThat().body("orders.get(0)._id", equalTo(orderId))
                .assertThat().body("orders.get(0).ingredients", equalTo(ingredients))
                .assertThat().body("orders.get(0).number", equalTo(orderNumber));
    }

    @Test
    public void testGetOrdersUserWithoutAuth() {
        Response regUser = regUser(newUser);
        String token = regUser.jsonPath().getString("accessToken");
        List<String> ingredients = getIngredients()
                .getBody()
                .path("data._id");
        Order order = new Order(ingredients);
        createOrder(token, order);
        Response getUsersOrders = getUsersOrders("");
        getUsersOrders
                .then()
                .statusCode(401)
                .assertThat().body("success", is(false))
                .assertThat().body("message", equalTo(NEED_AUTH));
    }
}
