package Day1Rest;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;

import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

public class RestAssuredProject {
	

	int id;

    @Test(priority=1)
    void GetUser() {
        given()
        .when()
        .get("https://reqres.in/api/users?page=2")
        .then()
        .statusCode(200)
        .body("page", equalTo(2))
        .log().all();
    }

    @Test(priority=2, dataProvider = "createUserData")
    void createUser(String name, String job) {
        // Creating user with dynamic data from the DataProvider
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("name", name);
        data.put("job", job);

        id = given()
            .contentType("application/json")
            .body(data)
            .when()
            .post("https://reqres.in/api/users")
            .jsonPath().getInt("id");

 
        // .then()
        // .statusCode(201)
        // .log().all();
    }

    @Test(priority=3, dependsOnMethods= {"createUser"})
    void UpdateUser() {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("name", "Rahul");
        data.put("job", "Engineer");
        
        given()
        .contentType("application/json")
        .body(data)
        .when()
        .put("https://reqres.in/api/users/" + id)
        .then()
        .statusCode(200)
        .log().all();
    }

    @Test(priority=4)
    void deleteUser() {
        given()
        .when()
        .delete("https://reqres.in/api/users/" + id)
        .then()
        .statusCode(204)
        .log().all();
    }

    @Test(priority=5)
    void GetUserById() {
        int userId = 5;  // Example user ID
        
        given()
        .when()
        .get("https://reqres.in/api/users/" + userId)
        .then()
        .statusCode(200)
        .body("data.id", equalTo(userId))
        .log().all();
    }

    // DataProvider for createUser test
    @DataProvider(name = "createUserData")
    public Object[][] createUserData() {
        return new Object[][] {
            {"Rahul", "Developer"},
            {"Subash", "QA"},
            {"Bikash", "TestEngineer"}
        };
    }


}
