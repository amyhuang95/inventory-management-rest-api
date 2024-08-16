# Inventory Management Rest API

This project creates backend REST APIs for performing CRUD operations in an inventory management system. The system leverages Redis to reduce database load and speed up data retrieval.

## Tech Stack
* **Server**: Java + Spring Boot
* **Database**: PostgreSQL
* **Cache Service**: Redis
* **Testing**: JUnit 5, Mockito

## Features
1. **RESTful API**: Developed using Spring Boot and Spring Data JPA/Hibernate for seamless CRUD operations and custom finder methods on inventory items stored in PostgreSQL.
2. **Caching**: Integrated with Redis to accelerate data retrieval, improving response times for frequently accessed data.
3. **Unit Testing**: Controller is unit tested using @WebMvcTest and Mockito; JPA repositories are unit tested using @DataJpaTest to ensure reliable and robust database interactions.

## Data Model
| Field        | Data Type | Description                              |
|--------------|------------|------------------------------------------|
| `id`         | `long`     | Unique identifier                        |
| `name`       | `string`   | Name of the inventory item               |
| `description`| `string`   | Description of the item                  |
| `unitPrice`  | `double`   | Price per unit                           |
| `quantity`   | `int`      | Available quantity                       |
| `category`   | `string`   | Category to which the item belongs       |
| `onSale`     | `boolean`  | Indicates if the item is on sale         |


## API Endpoints
| Methods | URLs                          | Actions                                       | Caching |
|---------|-------------------------------|-----------------------------------------------|---------|
| POST    | /api/inventory                | Create a new inventory item                   |         |
| GET     | /api/inventory                | Retrieve all inventory items                  | ✅       |
| GET     | /api/inventory/{:id}          | Retrieve an inventory item by id              | ✅       |
| PUT     | /api/inventory/{:id}          | Update an inventory item by id                |         |
| DELETE  | /api/inventory/{:id}          | Delete an inventory item by id                |         |
| DELETE  | /api/inventory                | Delete all iventory items                     |         |
| GET     | /api/inventory/on-sale        | Find on-sale inventory items                  | ✅       |
| GET     | /api/inventory?name=[keyword] | Find inventory items by name (keyword search) | ✅       |

## Future Improvements
1. [x] Add user authentication for secured API access.
2. [x] Implement pagination for large datasets in retrieval APIs.
3. [x] Expand the caching mechanism to other API operations.

## References
1. https://www.bezkoder.com/spring-boot-postgresql-example/
2. https://www.bezkoder.com/spring-boot-redis-cache-example/
3. https://bezkoder.com/spring-boot-unit-test-jpa-repo-datajpatest/
4. https://www.bezkoder.com/spring-boot-webmvctest/