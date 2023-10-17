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
        Faker faker = new Faker();
        return faker.internet().emailAddress();
    }
    public static String generatePassword(){
        Faker faker = new Faker();
        return faker.internet().password();
    }
    public static String generateFirstName(){
        Faker faker = new Faker();
        return faker.name().firstName();
    }

    public static  String generateLastName(){
        Faker faker = new Faker();
        return faker.name().lastName();
    }
}
