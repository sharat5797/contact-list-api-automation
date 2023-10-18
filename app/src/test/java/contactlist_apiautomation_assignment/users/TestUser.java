package contactlist_apiautomation_assignment.users;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import resources.APIResources;
import resources.Utils;
import resources.responsebody.user.AutenticationFailureResponse;
import resources.responsebody.user.CreateUserResponse;
import resources.responsebody.user.User;
import resources.testdata.user.TestDataBuild;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class TestUser {
    public static String token;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private String loginToken;

    @BeforeClass
    public void setUp() {
        firstName = Utils.generateFirstName();
        lastName = Utils.generateLastName();
        email = Utils.generateEmail();
        password = Utils.generatePassword();
    }

    @Test(priority = 0)
    public void shouldTestCreateUser() throws IOException {
        //Arrange
        TestDataBuild testDataBuild = new TestDataBuild();
        //Act
        CreateUserResponse createUserResponse = given().spec(Utils.requestSpecificationBuilder())
                .body(testDataBuild.createUserPayload(firstName, lastName, email, password))
                .when().post(APIResources.CreateUserAPI.getResource())
                .then().spec(Utils.responseSpecificationBuilder()).assertThat().statusCode(201)
                .extract().response().as(CreateUserResponse.class);
        token = createUserResponse.getToken();
        //Assert
        Assert.assertEquals(createUserResponse.getUser().getFirstName(), firstName);
    }

    @Test(priority = 1)
    public void shouldTestGetUserProfile() throws IOException {
        //Arrange
        //Act
        User user = given().spec(Utils.requestSpecificationBuilder())
                .header("Authorization", "Bearer " + token)
                .when().get(APIResources.GetUserProfileAPI.getResource())
                .then().spec(Utils.responseSpecificationBuilder())
                .assertThat().statusCode(200)
                .extract().response().as(User.class);
        //Assert
        Assert.assertEquals(user.getEmail(), email);
    }

    @Test(priority = 2)
    public void shouldTestUpdateUser() throws IOException {
        //Arrange
        TestDataBuild testDataBuild = new TestDataBuild();
        //Act
        User user = given().spec(Utils.requestSpecificationBuilder())
                .body(testDataBuild.createUserPayload(firstName, lastName, email, password))
                .header("Authorization", "Bearer " + token)
                .when().patch(APIResources.UpdateUserAPI.getResource())
                .then().assertThat().statusCode(200)
                .extract().response().as(User.class);
        //Assert
        Assert.assertEquals(user.getFirstName(), firstName);
    }

    @Test(priority = 3)
    public void shouldTestLogin() throws IOException {
        //Arrange
        TestDataBuild testDataBuild = new TestDataBuild();
        //Act
        CreateUserResponse createUserResponse = given().spec(Utils.requestSpecificationBuilder())
                .body(testDataBuild.createLoginPayload(email, password))
                .when().post(APIResources.LogInUserAPI.getResource())
                .then().assertThat().statusCode(200)
                .extract().response().as(CreateUserResponse.class);
        loginToken = createUserResponse.getToken();
        //Assert
        Assert.assertEquals(createUserResponse.getUser().getEmail(), email);
    }

    @Test(priority = 4)
    public void shouldTestLogOutUser() throws IOException {
        //Arrange

        //Act
        Response response = given().spec(Utils.requestSpecificationBuilder())
                .header("Authorization", "Bearer " + loginToken)
                .when().post(APIResources.LogOutUserAPI.getResource())
                .then().extract().response();
        //Assert
        Assert.assertEquals(response.statusCode(), 200);
    }

    @Test(priority = 5)
    public void shouldTestDeleteUser() throws IOException {
        //Arrange
        this.shouldTestLogin();

        //Act
        Response response = given().spec(Utils.requestSpecificationBuilder())
                .header("Authorization", "Bearer " + loginToken)
                .header("Cookie", "token=" + loginToken)
                .when().delete(APIResources.DeleteUserAPI.getResource())
                .then().extract().response();
        //Assert
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    //Negetive cases

    @Test(priority = 6)
    public void shouldNotCreateUserTest() throws IOException {
        /// Arrange: Prepare the test data and request details
        TestDataBuild testDataBuild = new TestDataBuild();

        // Act: Send a POST request to create a user
        Response response = given().spec(Utils.requestSpecificationBuilder())
                .body(testDataBuild.createUserPayload("TempUser", "K", email, "1234"))
                .when().post(APIResources.CreateUserAPI.getResource())
                .then().spec(Utils.responseSpecificationBuilder()).extract().response();

        // Assert: Verify the response
        response.then().assertThat().statusCode(400);
    }

    @Test(priority = 7)
    public void shouldNotGetUserProfileTest() throws IOException {
        //Arrange
        //Act
        Response response = given().spec(Utils.requestSpecificationBuilder())
                .header("Authorization", "Bearer " + "kjdnbcihusdcudsc")
                .when().get(APIResources.GetUserProfileAPI.getResource())
                .then().spec(Utils.responseSpecificationBuilder())
                .extract().response();
        //Assert
        response.then().assertThat().statusCode(401);
    }

    @Test(priority = 8)
    public void shouldNotUpdateUserTest() throws IOException {
        //Arrange
        TestDataBuild testDataBuild = new TestDataBuild();
        //Act
        Response response = given().spec(Utils.requestSpecificationBuilder())
                .body(testDataBuild.createUserPayload(firstName, lastName, email, password))
                .header("Authorization", "Bearer " + "sdjbcujb")
                .when().patch(APIResources.UpdateUserAPI.getResource()).then()
                .extract().response();
        //Assert
        response.then().assertThat().statusCode(401);
    }

    @Test(priority = 9)
    public void shouldNotLoginTest() throws IOException {
        //Arrange
        TestDataBuild testDataBuild = new TestDataBuild();
        //Act
        Response response = given().spec(Utils.requestSpecificationBuilder())
                .body(testDataBuild.createLoginPayload(email, "abckjsd"))
                .when()
                .post(APIResources.LogInUserAPI.getResource())
                .then()
                .extract().response();
        //Assert
        response.then().assertThat().statusCode(401);
    }

    @Test(priority = 10)
    public void shouldNotLogOutUserTest() throws IOException {
        //Arrange
        //Act
        AutenticationFailureResponse autenticationFailureResponse = given().spec(Utils.requestSpecificationBuilder())
                .header("Authorization", "Bearer " + "djkshfuisdcds")
                .when().post(APIResources.LogOutUserAPI.getResource())
                .then().extract().response().as(AutenticationFailureResponse.class);
        //Assert
        Assert.assertEquals(autenticationFailureResponse.getError(), "Please authenticate.");
    }

    @Test(priority = 11)
    public void shouldNotDeleteUserTest() throws IOException {
        //Arrange
        //Act
        Response response = given().spec(Utils.requestSpecificationBuilder())
                .header("Authorization", "Bearer " + loginToken)
                .when().delete(APIResources.DeleteUserAPI.getResource())
                .then().extract().response();
        //Assert
        response.then().assertThat().statusCode(401);
    }


}
