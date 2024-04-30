# User Registration project description

### KEY TECHNOLOGIES:
- Java 17
- Maven
- Spring Boot (version 3.2.3, used consistently across all Spring modules in this project)
- Spring Boot Web (RESTful API)
- Spring Boot Testing (JUnit, MockMvc, Mockito)
- Lombok
- MapStruct
- Swagger

### **GENERAL INFO**
**This app is designed according to all requirements**

- The app was designed according to the REST
- Data persistence layer was not used. Java List<> was used to simulate a database
- CustomGlobalExceptionHandler was used for error/exception handling
- User has the following fields:
1. Email (String; required). Validated against email pattern.
2. First name (String; required). 
3. Last name (String; required)
4. Birth date (LocalDate; required). Validated according to the needed date format and the minimum age requirement (located in the application.properties file)
5. Address (String; optional)
6. Phone number (String; optional)
- Additionally, user has userId (int) and isDeleted (boolean; implemented as 'soft delete') fields as in real databases.
- The app has the following endpoints:
1. POST: /auth/registration - endpoint for user registration
2. GET: /api/users?fromYear=&toYear= - endpoint for user search by birth years
3. PUT: /api/users/{userId} - endpoint for full updating of  user info by id
4. PATCH: /api/users/{userId} - endpoint for partial updating of user contact info by id
5. DELETE: /api/users/{userId} - endpoint for deleting of user by id
- 100% of controller and service level code is covered by unit tests (JUnit, MockMvc, Mockito)
- Additionally, Swagger was added to facilitate the study of the application 
