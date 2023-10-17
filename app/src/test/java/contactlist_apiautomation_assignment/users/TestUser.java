package contactlist_apiautomation_assignment.users;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import resources.APIResources;
import resources.Utils;
import resources.responsebody.user.CreateUserResponse;
import resources.responsebody.user.User;
import resources.testdata.user.TestDataBuild;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class TestUser {
    private String token;
    private String firstName;
    private String lastName;
    private String email = Utils.generateEmail();
    public String password = Utils.generatePassword();

    

    @Test(priority = 0)
    public void shouldTestCreateUser() throws IOException {
        //Arrange
        TestDataBuild testDataBuild = new TestDataBuild();
        //Act
        CreateUserResponse createUserResponse = given().spec(Utils.requestSpecificationBuilder())
                .body(testDataBuild.createUserPayload("TempUser", "K", email, password))
                .when().post(APIResources.CreateUserAPI.getResource())
                .then().spec(Utils.responseSpecificationBuilder()).assertThat().statusCode(201)
                .extract().response().as(CreateUserResponse.class);
        token = createUserResponse.getToken();
        System.out.println(token);
        //Assert
        Assert.assertEquals(createUserResponse.getUser().getFirstName(), "TempUser");
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
                .body(testDataBuild.createUserPayload("NewName", "NewLastName", email, password))
                .header("Authorization", "Bearer " + token)
                .when().patch(APIResources.UpdateUserAPI.getResource())
                .then().assertThat().statusCode(200)
                .extract().response().as(User.class);
        //Assert
        Assert.assertEquals(user.getFirstName(),"NewName");
    }


}
