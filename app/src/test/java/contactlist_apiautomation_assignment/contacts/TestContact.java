package contactlist_apiautomation_assignment.contacts;

import contactlist_apiautomation_assignment.users.TestUser;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import resources.APIResources;
import resources.Utils;
import resources.responsebody.contact.Contact;
import resources.responsebody.user.CreateUserResponse;
import resources.responsebody.user.User;
import resources.testdata.contact.ContactTestDataBuild;
import resources.testdata.user.TestDataBuild;

import java.io.IOException;
import java.util.List;

import static io.restassured.RestAssured.given;

public class TestContact {
    private String token;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String birthdate;
    private String street1;
    private String street2;
    private String city;
    private String stateProvince;
    private String postalCode;
    private String country;
    private String id;

    @BeforeClass
    public void setUp() throws IOException {
        password = Utils.generatePassword();
        firstName = Utils.generateFirstName();
        lastName = Utils.generateLastName();
//        birthdate=Utils.generateBirthdate();
        email = Utils.generateEmail();
//        phone=Utils.generatePhoneNumber();
        street1 = Utils.generateStreet1();
        street2 = Utils.generateStreet2();
        city = Utils.generateCity();
        stateProvince = Utils.generateStateProvince();
        postalCode = Utils.generatePostalCode();
        country = Utils.generateCountry();

        TestDataBuild testDataBuild = new TestDataBuild();
        CreateUserResponse createUserResponse = given().spec(Utils.requestSpecificationBuilder())
                .body(testDataBuild.createUserPayload("TempUser", "K", email, password))
                .when().post(APIResources.CreateUserAPI.getResource())
                .then().spec(Utils.responseSpecificationBuilder())
                .extract().response().as(CreateUserResponse.class);
        token = createUserResponse.getToken();
    }

    @Test(priority = 0)
    public void shouldTestCreateContact() throws IOException {
        //Arrange
        ContactTestDataBuild contactTestDataBuild = new ContactTestDataBuild();
        //Act
        Contact contact = given().spec(Utils.requestSpecificationBuilder())
                .header("Authorization", token)
                .body(contactTestDataBuild.createContactPayload(firstName, lastName, "1970-01-01", email, "9876543210", street1, street2, city, stateProvince, postalCode, country))
                .when().post(APIResources.CreateContactAPI.getResource())
                .then().spec(Utils.responseSpecificationBuilder()).assertThat().statusCode(201)
                .extract().response().as(Contact.class);
        //Assert
        Assert.assertEquals(contact.getFirstName(), firstName);
    }

    @Test(priority = 1)
    public void shouldTestGetContactList() throws IOException {
        //Arrange
        //Act
        List<Contact> contactList = given().spec(Utils.requestSpecificationBuilder())
                .header("Authorization", "Bearer " + token)
                .when().get(APIResources.GetContactListAPI.getResource())
                .then().spec(Utils.responseSpecificationBuilder())
                .assertThat().statusCode(200)
                .extract().response()
                .jsonPath().getList(".", Contact.class);
        id = contactList.get(0).getContact().get_id();
        //Assert
        Assert.assertEquals(contactList.size(), 1);
    }

    @Test(priority = 2)
    public void shouldTestGetContact() throws IOException {
        //Arrange
        //Act
        Contact contact = given().spec(Utils.requestSpecificationBuilder())
                .header("Authorization", "Bearer " + token)
                .when().get(APIResources.GetContactAPI.getResource() + id)
                .then().spec(Utils.responseSpecificationBuilder())
                .assertThat().statusCode(200)
                .extract().response().as(Contact.class);
        //Assert
        Assert.assertEquals(contact.get_id(), id);
    }

    @Test(priority = 3)
    public void shouldTestUpdateContact() throws IOException {
        //Arrange
        ContactTestDataBuild contactTestDataBuild = new ContactTestDataBuild();
        //Act
        Contact contact = given().spec(Utils.requestSpecificationBuilder())
                .body(contactTestDataBuild.createContactPayload("HelloUser", lastName, "1970-01-01", email, "9876543210", street1, street2, city, stateProvince, postalCode, country))
                .header("Authorization", "Bearer " + token)
                .when().put(APIResources.UpdateContactAPI.getResource() + id)
                .then().assertThat().statusCode(200)
                .extract().response().as(Contact.class);
        //Assert
        Assert.assertEquals(contact.getFirstName(), "HelloUser");
    }

    @Test(priority = 4)
    public void shouldTestUpdateContactPatch() throws IOException {
        //Arrange
        ContactTestDataBuild contactTestDataBuild = new ContactTestDataBuild();
        //Act
        Contact contact = given().spec(Utils.requestSpecificationBuilder())
                .body(contactTestDataBuild.updateContactPayload("HelloUser2"))
                .header("Authorization", "Bearer " + token)
                .when().patch(APIResources.UpdateContactAPI.getResource() + id)
                .then().assertThat().statusCode(200)
                .extract().response().as(Contact.class);
        //Assert
        Assert.assertEquals(contact.getFirstName(), "HelloUser2");
    }

    @Test(priority = 5)
    public void shouldTestDeleteContact() throws IOException {
        //Arrange
        //Act
        String response = given().spec(Utils.requestSpecificationBuilder())
                .header("Authorization", "Bearer " + token)
                .when().delete(APIResources.DeleteContactAPI.getResource() + id)
                .then().extract().response().asString();
        //Assert
        Assert.assertEquals(response, "Contact deleted");
    }

    //Negetive Cases
    @Test(priority = 6)
    public void shouldNotCreateContactTest() throws IOException {
        //Arrange
        ContactTestDataBuild contactTestDataBuild = new ContactTestDataBuild();
        //Act
        Response response = given().spec(Utils.requestSpecificationBuilder())
                .header("Authorization", token)
                .body(contactTestDataBuild.createContactPayload(firstName, lastName, "1970-01-01", email, "987654dhfgsudfygud3210", street1, street2, city, stateProvince, postalCode, country))
                .when().post(APIResources.CreateContactAPI.getResource())
                .then().spec(Utils.responseSpecificationBuilder())
                .extract().response();
        //Assert
        response.then().assertThat().statusCode(400);
    }

    @Test(priority = 7)
    public void shouldNotGetContactListTest() throws IOException {
        //Arrange
        //Act
        Response response = given().spec(Utils.requestSpecificationBuilder())
                .header("Authorization", "Bearer " + "jdhgfdusycgdsicstockernj")
                .when().get(APIResources.GetContactListAPI.getResource())
                .then().spec(Utils.responseSpecificationBuilder())
                .extract().response();
        //Assert
        response.then().assertThat().statusCode(401);
    }

    @Test(priority = 8)
    public void shouldNotGetContactTest() throws IOException {
        //Arrange
        //Act
        Response response = given().spec(Utils.requestSpecificationBuilder())
                .header("Authorization", "Bearer " + token)
                .when().get(APIResources.GetContactAPI.getResource() + "jguy654134yruy7")
                .then().extract().response();
        //Assert
        response.then().assertThat().statusCode(400);
    }

    @Test(priority = 9)
    public void shouldNotUpdateContactTest() throws IOException {
        //Arrange
        ContactTestDataBuild contactTestDataBuild = new ContactTestDataBuild();
        //Act
        Response response = given().spec(Utils.requestSpecificationBuilder())
                .body(contactTestDataBuild.createContactPayload("HelloUser", lastName, "1970-01-01", email, "987sdssdsjbsdhbsdcjnk6543210", street1, street2, city, stateProvince, postalCode, country))
                .header("Authorization", "Bearer " + token)
                .when().put(APIResources.UpdateContactAPI.getResource() + id)
                .then()
                .extract().response();
        //Assert
        response.then().assertThat().statusCode(400);
    }

    @Test(priority = 10)
    public void shouldNotUpdateContactPatchTest() throws IOException {
        //Arrange
        ContactTestDataBuild contactTestDataBuild = new ContactTestDataBuild();
        //Act
        Response response = given().spec(Utils.requestSpecificationBuilder())
                .body(contactTestDataBuild.updateContactPayload("HelloUser2"))
                .header("Authorization", "Bearer " + token)
                .when().patch(APIResources.UpdateContactAPI.getResource() + "dsjfhuis7438623")
                .then()
                .extract().response();
        //Assert
        response.then().assertThat().statusCode(400);
    }
    @Test(priority = 11)
    public void shouldNotDeleteContactTest() throws IOException {
        //Arrange
        //Act
        String response = given().spec(Utils.requestSpecificationBuilder())
                .header("Authorization", "Bearer " + token)
                .when().delete(APIResources.DeleteContactAPI.getResource() + "djcfhu8sdg632546718id")
                .then().extract().response().asString();
        //Assert
        Assert.assertEquals(response, "Invalid Contact ID");
    }
}
