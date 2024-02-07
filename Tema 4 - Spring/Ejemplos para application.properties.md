```java

resources/application.properties

# Fila comentada


debug=true

# TOMCAT u otro contenedor/servidor aplicaciones
# server.port=8080

#MongoDB
# spring.data.mongodb.uri=mongodb\://localhost/test2022
# spring.data.mongodb.host=mongoserver.example.com
# spring.data.mongodb.port=27017
# spring.data.mongodb.database=test
# spring.data.mongodb.username=user
# spring.data.mongodb.password=secret

#MySQL
#spring.jpa.hibernate.ddl-auto=update
#spring.datasource.url=jdbc:mysql://${MYSQL_HOST:localhost}:3306/db_example
#spring.datasource.username=springuser
#spring.datasource.password=ThePassword
#spring.datasource.driver-class-name =com.mysql.jdbc.Driver

#H2
# spring.jpa.database-platform=org.hibernate.dialect.H2Dialect



#Logging JPA Queries https://www.baeldung.com/sql-logging-spring-boot
#To beautify or pretty print the SQL
spring.jpa.properties.hibernate.format_sql=true
#log the SQL statements by configuring loggers in the properties file
#logs the SQL queries
logging.level.org.hibernate.SQL=DEBUG 
#logs the prepared statement parameters
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE 


#Logging JdbcTemplate Queries
logging.level.org.springframework.jdbc.core.JdbcTemplate=DEBUG
logging.level.org.springframework.jdbc.core.StatementCreatorUtils=TRACE

#
application.title=JPA_Prueba
application.formatted-version=1.0.2





Fichero resources/banner.txt
  ____             _                   _ ____   _      _   _ ____  
 / ___| _ __  _ __(_)_ __   __ _      | |  _ \ / \    | | | |___ \ 
 \___ \| '_ \| '__| | '_ \ / _` |  _  | | |_) / _ \   | |_| | __) |
  ___) | |_) | |  | | | | | (_| | | |_| |  __/ ___ \  |  _  |/ __/ 
 |____/| .__/|_|  |_|_| |_|\__, |  \___/|_| /_/   \_\ |_| |_|_____|
       |_|                 |___/                                   

${application.title}, ${application.formatted-version} and ${spring-boot.formatted-version}
```

#### Otro ejemplo con diversidad de configuraciones

```java

#Thymeleaf disconnect cache during development
spring.thymleaf.cache=false


# Server Port Configuration

# Configures the HTTP Port for the web container (e.g., Tomcat, Jetty, Netty).

# Default is 8080, but can be changed for running multiple services on the same instance.
# Example: 9090
server.port=9090 

  

# Spring Application Name

# Specifies the name of the Spring Boot application, helps in identifying the service at runtime.

spring.application.name=user-service # Example: user-service

  

# Spring Data Source Configuration

# Controls connections to databases. Includes URL, username, and password.

spring.datasource.url=jdbc:mysql://localhost:3306/mydb # Example: Database URL

spring.datasource.username=dbuser # Example: Database Username

spring.datasource.password=dbpassword # Example: Database Password

  

# Spring JPA Configuration

# Configures the JPA provider (e.g., Hibernate) for object-relational mapping.

spring.jpa.hibernate.ddl-auto=update # Auto-generates tables from entities

spring.jpa.show-sql=true # Logs every SQL query executed

  

# Spring Jackson Configuration

# Customizes JSON serialization/deserialization.

spring.jackson.serialization.indent_output=true # Pretty prints JSON for readability

spring.jackson.date-format=yyyy-MM-dd HH:mm:ss # Custom date format in JSON

spring.jackson.time-zone=UTC # Sets timezone for JSON serialization

  

# Server Servlet Context Path

# Sets a prefix for all HTTP request URLs, useful for API versioning.

server.servlet.context-path=/api/v1 # Example: /api/v1

  

# Spring Security Configuration

# Configures Spring Security features, like user details and OAuth2 client registrations.

spring.security.user.name=admin # Example: Default user name

spring.security.user.password=secret # Example: Default user password

  

# Management Endpoints Web Exposure

# Controls the exposure of actuator endpoints for monitoring and management.

management.endpoints.web.exposure.include=health,info # Example: Expose specific endpoints

  

# Spring Threads Virtual Enabled (Java 21)

# Enables the use of Virtual Threads in Spring applications.

spring.threads.virtual.enabled=true # Enables Virtual Threads
```