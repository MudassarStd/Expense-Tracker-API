# Expense Tracker API

A Simple RESTful API built with Kotlin and Spring Boot for tracking personal expenses. This application provides an efficient way to manage and analyze expenses with secure authentication.

## Technologies Used

- Kotlin 1.9.25
- Spring Boot 3.4.4
- Spring Data JPA
- Spring Security
- H2 Database || MySQL
- Gradle
- HTML/CSS/JavaScript (Static Frontend to test or playaround)

## Highlighted Features
- Expenses are secured via JWT based authentication
- Robust Token validation with JWT filter
- Pagination
- Centralized Exception Handling

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/Expense-Tracker-API.git
cd Expense-Tracker-API
```

### Build the Project

```bash
./gradlew build
```

### Run the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

## Accessing the Frontend

Once the application is running, you can access the frontend interface by navigating to:
```
http://localhost:8080/auth.html
```

The frontend provides a user-friendly interface to:
- Sign up for a new account
- Sign in to existing account
- View all expenses
- Add new expenses
- Update existing expenses
- Delete expenses
- Filter and sort expenses

## Authentication API Endpoints

- `POST /auth/register` - Register a new user
  - Request Body:
    ```json
    {
      "name": "User Name",
      "email": "user@example.com",
      "password": "password"
    }
    ```
- `POST /auth/login` - Login with existing credentials
  - Request Body:
    ```json
    {
      "email": "user@example.com",
      "password": "password"
    }
    ```

## Response:

Returns a JWT token upon successful registration.

## Authorization 🔒:
Use the JWT token returned from the /auth/register or /auth/login endpoints for all subsequent API requests.

## Expense API Endpoints

### Basic CRUD Operations

- `GET /expense/find/{id}` - Get a specific expense by ID
- `GET /expense` - Get all expenses with optional filtering
  - Query Parameters:
    - `minAmount`: Filter by minimum amount
    - `maxAmount`: Filter by maximum amount
    - `category`: Filter by category
    - `type`: Filter by type
    - `startDate`: Filter by start date
    - `endDate`: Filter by end date
- `POST /expense` - Create a new expense
- `POST /expense/list` - Create multiple expenses at once
- `PUT /expense/update/{id}` - Update an existing expense
- `DELETE /expense/delete/{id}` - Delete an expense

### Analytics Endpoints

- `GET /expense/total` - Get total amount of expenses
  - Query Parameters:
    - `startDate`: Optional start date for period
    - `endDate`: Optional end date for period
- `GET /expense/total/type` - Get total amount by expense type
  - Query Parameter:
    - `type`: Required expense type
- `GET /expense/get/sorted` - Get all expenses sorted
  - Query Parameter:
    - `order`: Optional sort order (ASC or DEC)

## Database Configuration

The application uses H2 in-memory database by default. The database console is available at `http://localhost:8080/h2-console` when the application is running.

## Project Structure

```
src/
├── main/
│   ├── kotlin/
│   │   └── com/
│   │       └── std/
│   │           ├── controller/    
│   │           ├── service/       
│   │           ├── model/         
│   │           ├── repository/    
│   │           ├── dto/          
│   │           ├── mapper/       
│   │           ├── exception/        
│   │           └── config/security       
│   ├── resources/
│   │   ├── static/              
│   │   └── templates/           
│   └── application.properties  
└── test/
    └── kotlin/                  
```
