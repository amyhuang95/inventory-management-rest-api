# Inventory Management Restful API
REST API for CRUD operations on a inventory management system using Spring Boot + PostgreSQL

## Features
1. Each `Inventory` item has `id`, `name`, `description`, `unit price`, `quantity`, `category`, `on-sale`.
2. APIs help to create, retrieve, update, delete `Inventory`.
3. APIs also support custom finder methods such as find by name or by on-sale status.

## API Methods
| Methods | URLs                          | Actions                                   |
|---------|-------------------------------|-------------------------------------------|
| POST    | /api/inventory                | create new Inventory                      |
| GET     | /api/inventory                | retrieve all Inventory                    |
| GET     | /api/inventory/{id}           | retrieve a Inventory by id                |
| PUT     | /api/inventory/{id}           | update a Inventory by id                  |
| DELETE  | /api/inventory/{id}           | delete a Inventory by id                  |
| DELETE  | /api/inventory                | delete all Inventory                      |
| GET     | /api/inventory/on-sale        | find all inventory that are on-sale       |
| GET     | /api/inventory?name={keyword} | find all Item which name contains keyword |