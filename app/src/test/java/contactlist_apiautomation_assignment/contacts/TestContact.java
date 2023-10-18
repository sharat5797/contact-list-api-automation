package contactlist_apiautomation_assignment.contacts;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import resources.APIResources;
import resources.Utils;
import resources.responsebody.contact.Contact;
import resources.responsebody.user.CreateUserResponse;
import resources.testdata.contact.ContactTestDataBuild;
import resources.testdata.user.TestDataBuild;

import java.io.IOException;

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

    @BeforeClass
    public void setUp() throws IOException {
        password = Utils.generatePassword();
        firstName=Utils.generateFirstName();
        lastName=Utils.generateLastName();
//        birthdate=Utils.generateBirthdate();
        email = Utils.generateEmail();
//        phone=Utils.generatePhoneNumber();
        street1=Utils.generateStreet1();
        street2=Utils.generateStreet2();
        city=Utils.generateCity();
        stateProvince=Utils.generateStateProvince();
        postalCode=Utils.generatePostalCode();
        country=Utils.generateCountry();



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
                .header("Authorization",token)
                .body(contactTestDataBuild.createContactPayload(firstName,lastName,"1970-01-01",email,"9876543210",street1,street2,city,stateProvince,postalCode,country))
                .when().post(APIResources.CreateContactAPI.getResource())
                .then().spec(Utils.responseSpecificationBuilder()).assertThat().statusCode(201)
                .extract().response().as(Contact.class);
        //Assert
        Assert.assertEquals(contact.getFirstName(), firstName);
    }
}
