# AirMood
# 🎥🎥BooktopiaHub🎥🎥

# Project decription:
- 🫡Welcome to the BooktopiaHub project!🫡
- 😉This is a web application that provides various features including authentication, registration, and CRUD (Create, Read, Update, Delete) operations😉
- The project is built using the Hibernate and Spring frameworks, Spring Boot, which provide powerful tools for interacting with databases and developing robust web applications.
- Also project is built using liquibase and docker.
- With the BooktopiaHub, users can enjoy a simplified and user-friendly experience while managing book-store data and operations.
  
## Setup

To set up the project, follow these steps:

### Prerequisites

Make sure you have the following software installed on your system:

- Java Development Kit (JDK) 11 or higher
- Apache Maven
- Apache Tomcat vesion 9 or higher
- DataBase: PostgresSQL
- docker
- liquibase

### Installation
- First of all, you should made your fork
- Second, clink on Code<> and clone link, after that open your Intellij Idea, click on Get from VCS
- past link, which you clone later

### Installation
- First of all, you should made your fork
- Second, clink on Code<> and clone link, after that open your Intellij Idea, click on Get from VCS
- past link, which you clone later

### Replace Placeholders:
To connect to your DB, you should replace PlaceHolders in .env and application.properties
- Open package resources and open file env and application.properties in your project.
- Locate the placeholders that need to be replaced.
- These placeholders might include values such as
- spring.datasource.username=$POSTGRES_USER = replace with your Postgres
- spring.datasource.password=$POSTGRES_PASSWORD = replace with your password Postgrese
  
## And in .env file
- POSTGRES_USER=postgres
- POSTGRES_PASSWORD=123456 // change to your password
- POSTGRES_DATABASE=blog // change to your db
- POSTGRES_LOCAL_PORT=5434 // chane to your port
- POSTGRES_DOCKER_PORT=5432 // change to your docker port
- SPRING_LOCAL_PORT=8088 // change to your local port
- SPRING_DOCKER_PORT=8080 // change to your docker port
- DEBUG_PORT=5005 // change to your port

# Features 🤌:

## User  🤵‍♂️
- Registration like a user
- Authentication like a user
- Create/update/remove a user

## Book 📕
- Create/update/remove a book
- Find book by id
- Sorting books by parameters
- Display all available books

## Shopping-cart 💵
- Get all shopping-carts
- Get shopping-cart by user

## Cart-Item 💵
- Get cart-item by book id
- Create/update/remove a cart-item

## Order 💵
- Create/update/remove a order
- Find order by id
- Get order history by user
- Ger order by order id and orderItem id

## Order-Item 💵
- Ger orderItem by Book id
- Find orderItme by id
- Remove a orderItem


## Category ⏱
- Create/update/remove a category
- Display all available categories
- Find category by id


## Role 🙎‍♂️
- Create/update/remove a role
- Get role by roleName
