# Library Management System

## Overview
A Java Spring Boot-based Library Management System that allows users to borrow and return books with appropriate validation and error handling.

## Key Features:
- Add a new Book
- View all available books
- check book availability
- Borrowing books
- Return a book
- Error handling and validation

## Prerequisites
- Java 17 or later
- Maven
- PostgreSQL or any other DataBase
- Update the following details according to your database into the application.properties file:
  - `DB_URL`: URL for the database connection
  - `DB_USERNAME`: Database username
  - `DB_PASSWORD`: Database password
  - `DB_DIALECT`: Database specific dialect

## Technology used
- Spring boot
- Java
- Junit and Mockito
- Version Control (Git, Github)

 ## Installation

 1. Clone the repository:
   ```bash
      git clone https://github.com/SMITP1483/Library-Management-System
```
2. Navigate to the project directory:
```bash
   cd Library-Management-System
```
3. Install dependencies using Maven:
``` bash
    mvn clean install
```
4. Update the application.properties file according to your database

## Usage

- After starting the project, you can use it as follows:

#### Access the Web Interface

- Open the postman or any other tool to pass the body argument in json format (Currently there are not an user interface)

#### API Endpoints

1. Add a New Book
   
   - Method: 'POST'
   - EndPoint: 'api/Books'
   - Description: Adds a new book to the database.
   - Request Body: JSON object representing the book.
``` bash
    {
        "isbnNo": "0-123-45678-9",
        "title": "Clean Code",
        "authorName": "Robert C. Martin",
        "publicationYear": 2008,
        "available": true
    }
```

2. Get All Available Books
   
   - Method: 'GET'
   - EndPoint: 'api/Books'
   - Description: Get all available books from the database.

3. Borrow Book

   - Method: 'POST'
   - EndPoint: 'api/BorrowBook'
   - Description: Borrow a boook
   - Request Body: JSON object representing the borrow details.
``` bash
    {
    "bookIsbnNo": "978-0-596-68-7",
    "userDetails": {
      "id": 3,
      "firstName": "Jack",
      "lastName": "Alice",
      "email": "Jack@gmail.com"
    },
    "borrowedDate": "2024-08-31T10:30:00",
    "returnedDate": null,
    "isReturned": false
  }
```
4. Return Book

   - Method: 'POST'
   - EndPoint: 'api/ReturnBook'
   - Description: Return a boook
   - Request Body: JSON object representing the book isbn no. to update the book availibility status.
``` bash
    {
      "isbnNo": "978-0-596-68-7"
    }
```

## Testing

Unit tests for the project are located in the src/test/java/org/example/librarymanagementsystem directory. You will find four different test files that cover various aspects of the application.
     





  
