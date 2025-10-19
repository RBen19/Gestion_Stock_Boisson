# Gestion Stock Boisson

Gestion Stock Boisson is a comprehensive beverage stock management system designed to track inventory, manage suppliers, and handle stock movements efficiently.

## Search strategy 

this project implement logic search strategy like : 
 - FEFO
 - FIFO
 - LIFO


## Technology Stack

This project is built using a robust and modern Java-based technology stack:

- **Backend Framework:** Jakarta EE 10
- **RESTful Web Services:** Jersey (JAX-RS implementation)
- **Database:** PostgreSQL
- **Object-Relational Mapping (ORM):** Hibernate (JPA implementation)
- **Authentication & Security:** JSON Web Tokens (JWT) with jBCrypt for password hashing
- **Dependency Injection:** Contexts and Dependency Injection (CDI)
- **JSON Processing:** Jackson
- **Build Tool:** Apache Maven

## Project Structure

The project is organized into logical modules to ensure a clear separation of concerns:

```
src/main/java/org/beni/gestionboisson/
├── auth/         # Handles user authentication, roles, and security.
├── boisson/      # Manages beverages and their categories.
├── emplacement/  # Manages physical storage locations.
├── fournisseur/ # Manages suppliers.
├── lignemouvement/ # Tracks individual lines within a stock movement.
├── lot/          # Manages batches of products.
├── mouvement/   # Handles stock movements (e.g., entry, exit).
├── shared/       # Shared utilities, configurations, and base classes.
└── ...           # Other feature modules.
```

## API Endpoints

The application exposes a RESTful API for interacting with the system. Below is a high-level overview of the available endpoints.

- `/api/auth`: User authentication, registration, and token management.
- `/api/boissons`: CRUD operations for beverages.
- `/api/categories`: CRUD operations for beverage categories.
- `/api/fournisseurs`: CRUD operations for suppliers.
- `/api/emplacements`: CRUD operations for storage locations.
- `/api/mouvements`: Operations for managing stock movements.

## Setup and Deployment

### Prerequisites

- Java 21 or later
- Apache Maven 3.8+
- PostgreSQL Database
- A Jakarta EE 10 compatible application server (e.g., WildFly, Payara, GlassFish)

### Database Configuration

1.  Create a new PostgreSQL database.
2.  Configure the database connection details inside `src/main/resources/META-INF/persistence.xml`. You will need to create this file if it does not exist.

Example `persistence.xml`:
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="https://jakarta.ee/xml/ns/persistence https://jakarta.ee/xml/ns/persistence/persistence_3_0.xsd"
             version="3.0">
  <persistence-unit name="default">
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
    <jta-data-source>java:jboss/datasources/YourDataSource</jta-data-source> <!-- Example for WildFly -->
    <properties>
      <property name="jakarta.persistence.jdbc.driver" value="org.postgresql.Driver"/>
      <property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/your_db_name"/>
      <property name="jakarta.persistence.jdbc.user" value="your_username"/>
      <property name="jakarta.persistence.jdbc.password" value="your_password"/>
      <property name="hibernate.hbm2ddl.auto" value="update"/>
      <property name="hibernate.show_sql" value="true"/>
      <property name="hibernate.format_sql" value="true"/>
    </properties>
  </persistence-unit>
</persistence>
```

### Build

To build the project, run the following Maven command from the root directory. This will compile the source code and package it into a `.war` file in the `target/` directory.

```bash
mvn clean install
```

### Deployment

Deploy the generated `target/gestionBoisson-1.0-SNAPSHOT.war` file to your Jakarta EE application server.
