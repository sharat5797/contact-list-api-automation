package resources;




public enum APIResources {
    CreateUserAPI("/users"),
    GetUserProfileAPI("/users/me"),
    UpdateUserAPI("/users/me"),
    LogOutUserAPI("/users/logout"),
    LogInUserAPI("/users/login"),
    DeleteUserAPI("/users/me"),
    CreateContactAPI("/contacts"),
    GetContactListAPI("/contacts"),
    GetContactAPI("/contacts/"),
    UpdateContactAPI("/contacts/"),
    DeleteContactAPI("/contacts/");

    public String getResource() {
        return resource;
    }

    private final String resource;

    APIResources(String resource) {
        this.resource = resource;
    }
}
