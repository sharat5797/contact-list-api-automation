package resources;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import com.github.javafaker.Faker;
import java.io.*;
import java.util.Properties;

public class Utils {
    static PrintStream log;

    static {
        try {
            log = new PrintStream(new FileOutputStream("src/logging.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static RequestSpecification requestSpecificationBuilder() throws IOException {
        return new RequestSpecBuilder()
                .setBaseUri(getGlobalValue("baseURL"))
                .setContentType(ContentType.JSON)
                .addFilter(RequestLoggingFilter.logRequestTo(log))
                .addFilter(ResponseLoggingFilter.logResponseTo(log))
                .build();

    }

    public static ResponseSpecification responseSpecificationBuilder() {
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON).build();
    }

    public static String getGlobalValue(String key) throws IOException {
        Properties properties = new Properties();
        FileInputStream file;
        file = new FileInputStream("src/test/java/resources/global.properties");
        properties.load(file);
        return properties.getProperty(key);
    }

    public static String getJsonPath(Response response, String key) {
        String resp = response.asString();
        JsonPath jsonPath = new JsonPath(resp);
        return jsonPath.get(key);
    }

    public static String generateEmail(){
        return new Faker().internet().emailAddress();
    }
    public static String generatePassword(){
        return new Faker().internet().password();
    }
    public static String generateFirstName(){
        return new Faker().name().firstName();
    }

    public static  String generateLastName(){
        return new Faker().name().lastName();
    }
    public static String generatePhoneNumber() {
        return new Faker().phoneNumber().phoneNumber();
    }
    public static String generateBirthdate() {
        return new Faker().date().birthday(18, 65).toString();
    }

    public static String generateStreet1() {
        return new Faker().address().streetAddress();
    }

    public static String generateStreet2() {
        return new Faker().address().secondaryAddress();
    }

    public static String generateCity() {
        return new Faker().address().city();
    }

    public static String generateStateProvince() {
        return new Faker().address().stateAbbr();
    }

    public static String generatePostalCode() {
        return new Faker().address().zipCode();
    }

    public static String generateCountry() {
        return new Faker().address().country();
    }

    public static String generateOwner() {
        return new Faker().regexify("[a-f0-9]{24}");
    }

    public static int generateV() {
        return new Faker().number().numberBetween(0, 10);
    }

}
