# EasyShop — E-Commerce API

## Description of the Project

EasyShop is a Java Spring Boot REST API that powers an online store. It serves a catalog of categories and products, supports filtered product search, and gives every registered user a secure shopping cart and an editable profile. Authentication is handled with JSON Web Tokens (JWT), and the API enforces role-based access so that shoppers can browse the store and manage their own cart while only admins can create, update, or delete catalog data. The API is backed by a MySQL database through Spring Data JPA and is consumed by a prebuilt EasyShop web client. This capstone also fixes two production bugs — a broken product search and an incomplete product update — each proven by automated tests across the service, web, and data layers.

**Slogan:** *"Shop Smart, Shop Easy"*

## User Stories

As a shopper, I want product search to return every product that matches my filters (category, minimum price, maximum price, and subcategory) and not only the featured ones so that I can see the full catalog instead of a partial list.

As an admin, I want every field I edit on a product (name, price, category, description, subcategory, stock, featured, and image) to actually save so that changes like stock updates don't silently disappear.

As a developer, I want unit tests (using JUnit and Mockito) that fail on the broken code and pass after the fix so that both bugs stay fixed.

As a shopper, I want to browse all product categories (Electronics, Fashion, Home & Kitchen) so that I can navigate the store by department.

As a shopper, I want to view a single category by its id so that I can read its name and description.

As a shopper, I want to see every product inside a category (for example GET /categories/1/products) so that I can shop one department at a time.

As a shopper, I want to filter products by category, minimum price, maximum price, and subcategory so that I can narrow the catalog down to exactly what I want.

As a shopper, I want to view the full details of a single product (name, price, description, stock, and image) so that I can decide before adding it to my cart.

As a new user, I want to register for an account with a username and password so that I can have my own cart and profile.

As a returning user, I want to log in and receive a JWT token so that the API recognizes me on every request.

As an admin, I want to add a new category so that I can create new departments in the store.

As an admin, I want to update an existing category so that I can correct its name or description.

As an admin, I want to delete a category so that I can remove departments the store no longer carries.

As an admin, I want to add a new product (with price, category, description, subcategory, stock, featured flag, and image) so that I can list new items for sale.

As an admin, I want to update a product so that I can change its price, description, stock, or image at any time.

As an admin, I want to delete a product so that discontinued items no longer appear in the store.

As a logged-in shopper, I want to view my shopping cart with each product's full details and a running total so that I know exactly what I am about to buy.

As a logged-in shopper, I want to add a product to my cart (POST /cart/products/15) and have the quantity increase if it is already there so that I never get duplicate cart rows.

As a logged-in shopper, I want to change the quantity of a product in my cart so that I can buy more or fewer of an item.

As a logged-in shopper, I want to clear my entire cart in one action so that I can start over quickly.

As a logged-in shopper, I want to view and update my profile (first name, last name, phone, email, address, city, state, and zip) so that my account details stay current.

## Setup

Instructions on how to set up and run the project using IntelliJ IDEA.

### Prerequisites

- IntelliJ IDEA: Ensure you have IntelliJ IDEA installed, which you can download from [here](https://www.jetbrains.com/idea/download/).
- Java SDK: Make sure Java 17 (or higher) is installed and configured in IntelliJ.
- MySQL: Install MySQL Server and MySQL Workbench.
- A REST client such as Insomnia (a collection is included: `capstone-insomnia-collection.yaml`).

### Database Setup

1. Open MySQL Workbench.
2. Open and run `database/create_database_easyshop.sql` to create the `easyshop` database, all tables, and the seed data.
3. In `src/main/resources/application.properties`, set the datasource URL, username, and password to match your local MySQL instance.

### Running the Application in IntelliJ

Follow these steps to get the API running within IntelliJ IDEA:

1. Open IntelliJ IDEA.
2. Select "Open" and navigate to the `capstone-api-starter` directory.
3. Wait for IntelliJ to index the files and download the Maven dependencies.
4. Find the `ECommerceApplication` class in `src/main/java/org/yearup/`.
5. Right-click the file and select 'Run ECommerceApplication.main()' to start the API on `http://localhost:8080`.
6. Open the EasyShop web client (`capstone-client-easyshop/index.html`) in your browser to use the storefront against the running API.

## Technologies Used

- Java 17
- Spring Boot (Web, Validation)
- Spring Data JPA + Hibernate
- Spring Security with JWT authentication and role-based authorization
- MySQL
- Maven (with the included Maven wrapper `mvnw`)
- JUnit 5, Mockito, @WebMvcTest, and @DataJpaTest (with in-memory H2) for testing
- Insomnia for endpoint testing
- **Git & GitHub** for version control



## DEMO









## Future Work

- Add a checkout/orders flow that converts a cart into a saved order with order history.
- Add product reviews and star ratings.
- Add pagination and sorting to the product search.
- Add an admin dashboard endpoint for low-stock alerts.

## Resources

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/index.html)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/reference/index.html)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/index.html)
- [Baeldung — Spring Security with JWT](https://www.baeldung.com/spring-security-oauth-jwt)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://site.mockito.org/)
- https://raymaroun.github.io/yearup-java-visuals/

## Team Members

- **David Amah** - Solo developer. Fixed the product search and update bugs (proven with service, web, and data-layer tests), built the Categories CRUD API, built the shopping cart with stream-based assembly, and added the user profile endpoints — all secured with JWT and role-based authorization.

## Thanks

Express gratitude towards those who provided help, guidance, or resources:

- Thank you to Raymond for continuous support and guidance.