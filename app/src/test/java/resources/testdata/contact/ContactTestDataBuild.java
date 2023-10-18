package resources.testdata.contact;

import resources.requestbody.contact.CreateContactPayload;

public class ContactTestDataBuild {
    public CreateContactPayload createContactPayload(
            String firstName,
            String lastName,
            String birthdate,
            String email,
            String phone,
            String street1,
            String street2,
            String city,
            String stateProvince,
            String postalCode,
            String country
    ) {
        CreateContactPayload createContactPayload = new CreateContactPayload();
        createContactPayload.setFirstName(firstName);
        createContactPayload.setLastName(lastName);
        createContactPayload.setBirthdate(birthdate);
        createContactPayload.setEmail(email);
        createContactPayload.setPhone(phone);
        createContactPayload.setStreet1(street1);
        createContactPayload.setStreet2(street2);
        createContactPayload.setCity(city);
        createContactPayload.setStateProvince(stateProvince);
        createContactPayload.setPostalCode(postalCode);
        createContactPayload.setCountry(country);
        return createContactPayload;
    }
}
