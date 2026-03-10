# E-Commerce Web Application

A full-stack **online shopping platform** that allows users to browse products, add items to cart, and place orders. The application manages product inventory and user carts while demonstrating core backend development concepts using **Spring Boot and PostgreSQL**.

## Features

* Product listing and catalog management
* Add to cart functionality
* Order placement and management
* Inventory/stock management
* Backend REST APIs using Spring Boot
* Database integration with PostgreSQL
* Simple responsive UI using HTML and CSS

## Tech Stack

**Backend:** Java, Spring Boot, Spring Data JPA, REST APIs
**Database:** PostgreSQL
**Frontend:** HTML, CSS, JavaScript
**Tools:** Eclipse, Postman, Git, GitHub, Maven

## API Overview

* **GET** /api/products — Fetch all products
* **GET** /api/products/{id} — Fetch product by ID
* **POST** /api/cart/add — Add product to cart
* **GET** /api/cart — View cart items
* **POST** /api/order — Place order

## Project Architecture

```
/ecommerce-project
 ├── src
 │   ├── main
 │   │   ├── java
 │   │   │    └── com.ecommerce
 │   │   │         ├── controller
 │   │   │         ├── service
 │   │   │         ├── repository
 │   │   │         └── model
 │   │   ├── resources
 │   │        └── application.properties
 ├── pom.xml
```

## Running the Project

**Backend:**

```
mvn spring-boot:run
```

Then open browser:

```
http://localhost:8080
```

## Author

Adarsh Gaon
