# calendarly-svc
***

This project is implements the backend logic for the calendarly project using Java Spring Boot. The calendarly app is aimed at helping planning events with friends and groups easier.

## Implementation

- There are 3 main classes: Users, Groups, and Events. All of these entities are linked through common data points (ie. account ID, group ID, etc), hence, a PostgreSQL database was used to manage the one-to-many and many-to-many relationships.
- The services for each of the entities implement CRUD functionality such as creating the user accounts, joining groups based on a given group ID, and more.

## How it works

To run and see the program working, you will need to have a local PostgreSQL database and Postman installed.

Update the application.properties file  to connect with the database:
```JAVA
spring.datasource.url=jdbc:postgresql://localhost:<port>/<your_database_name>
spring.datasource.username=<your_database_username>
spring.datasource.password=<your_database_password>
```


