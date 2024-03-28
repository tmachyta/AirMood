# ✈️✈️AirMood✈️✈️

# Project Description:
- 🫡Welcome to the AirMood project!🫡
- 😉This is a web application that provides various features and CRUD (Create, Read, Update, Delete) operations😉
- The project is built using the Hibernate and Spring frameworks, specifically Spring Boot, which provide powerful tools for interacting with databases and developing robust web
- Also project is built using liquibase and docker.
- With the AirMood, users can enjoy a simplified and 😉user-friendly😉 experience while managing air-companies, airplanes and flights data and operations

## Setup

To set up the project, follow these steps:

### Prerequisites

Make sure you have the following software installed on your system:

- Java Development Kit (JDK) 17 or higher
- Spring Boot 2.2 or higher
- Apache Maven
- Apache Tomcat vesion 9 or higher
- DataBase: MySQL
- docker

### Installation
- First of all, you should made your fork
- Second, clink on Code<> and clone link, after that open your Intellij Idea, click on Get from VCS
- past link, which you clone later

### Replace Placeholders:
To connect to your DB, you should replace PlaceHolders in .env
- Open package resources and open file env in your project.
- Locate the placeholders that need to be replaced.
- These placeholders might include values such as
- MYSQL_USER= YOUR_USERNAME -> replace with your MySQL_DB
- MYSQL_PASSWORD=YOUR_PASSWORD -> replace with your password to your MySQL_DB
- MYSQL_LOCAL_PORT=YOUR_LOCAL_PORT -> replace with your local port
- MYSQL_DOCKER_PORT=YOUR_DOCKER_PORT -> replace with your docker port
- SPRING_LOCAL_PORT=YOUR_SPRING_LOCAL_PORT -> replace with your spring local port
- SPRING_DOCKER_PORT=YOUR_DOCKER_PORT -> replace with your docker port
- DEBUG_PORT=5006

# Features 🤌:

## 🏢 Air Company  🏢
- Create a new air company
- Display all air companies
- Find air company by id
- Soft-Delete air company by id
- Update air company by id

## ✈️ Airplane ✈️
- Create a new airplane
- Create a new airplane without adding air company
- Update airplane by adding air company
- Move airplane between companies
- Display all airplanes
- Find airplane by id
- Soft-Delete airplane by id

## ⏰ Flight ⏰
- Create a new flight
- Display all flights by air company and status
- Update flight status to delayed and delayStartedAt
- Update flight status to active and startedAt
- Update flight status to completed and endedAt
- Display all flights by status active and startedAt time more then 24 hours
- Display all flights by status completed and time difference between
started and ended time is bigger than the estimated flight time.
- Display all flights
- Find flight by id
- Soft-Delete flight by id

# Controllers 🕹

## Auth
- Post - /register
- Post - /login

## Car
- Get | display all cars - /cars
- Post | add car to repository - /cars
- Get | find car by id - /cars/{id}
- Delete | soft delete car by id - /cars{id}
- Put | update - /cars{id}

## User
- Get | display all users - /users
- Delete | soft delete user by id - /users/{id}
- Get | find user information by id - /users/me/{id}
- Put | update user information by id - /users/me/{id}
- Put | update user role by id - /users/role/{id}
- Put | update user role by email - /users/role/{email}

## Rental
- Get | display all rentals - /rentals
- Post | save rental to repositort - /rentals
- Get | find rental by id - /rentals/{id}
- Get | find rental by car id - /rentals/car/{id}
- Get | find rental by user id - /rentals/user/{id}
- Put | update rental by id - /rentals/{id}
- Delete | soft delete by id - /rentals/{id}

## Payment
- Get | display all payments - /payments
- - Post | save payment to repository - /payments
- Get | find payment by id - /payments/{id}
- Get | find payment by rental id - /payments/rental/{id}
- Get | find payment by user id - /payments/user/{id}
- Post | create success payment session - /payments/create-session
- Get | get success payment session - /payments/success
- Get | get cancel payment by id - /payments/cancel/{id}

