# Money transfer Rest API

A Java RESTful API for money transfers between users accounts

### Technologies
- Spring Boot API
- H2 in memory database
- Log4j
- Docker
- Jacoco (code coverage tool)
- Swagger API Documentation

### How to run

- Checkout Code from develop branch
  -  https://github.com/thakaresureshss/moentransferapp.git
- Build Project(Goto inside checked out folder)
 ```sh
  mvn clean install
```
- Run Jar 
  -  Use Below command 
```sh
java -jar moneytransfer-0.0.1-SNAPSHOT.jar
```
- Test Application 

Application starts a embedded server on localhost port 8080 An H2 in memory database initialized with some sample user and account data To view, Use Swagger to test application using below url

- http://localhost:8080/swagger-ui.html

-  Steps to Folllow 
    -  Create First Customer 
    -  Create First Account 
    -  Link Account to Customer 
    -  Repeat Above 3 step for second Customer and Account 
    -  Transfer Amount from Account 1 to Account 2
    -  Check balance of Account 1 and 2 
    -  Check Transactions for Account 1 and Account 2.
    -  

### Code Coverage Enpoints
- Use below command inside the root directory of code 
   -  Use Below command 
```sh
 mvn test jacoco:report
```

### Important Enpoints
-   http://localhost:8080/v1/customers/add
-   http://localhost:8080/v1/accounts/add
-   http://localhost:8080/v1/accounts/1/link/customer/2
-   http://localhost:8080/v1/accounts/transfer/1
-   http://localhost:8080/v1/accounts/2
-   http://localhost:8080/v1/accounts/transactions/1

### Available Services

- Add Customer
- Update Customer Details
- Delete Customer 
- Retrieve Customer Details
- List Details of All Customers 
- Add Account
- Update Account Details
- Delete Account 
- Retrieve Account Details
- List Details of All Accounts 
- Link Account to Custommer
- Transfer money from once account to other
- View Balance for both account (Retrieve Account Details)
- View Transaction Details for both account.


### Http Status
- 200 OK: The request has succeeded
- 400 Bad Request: The request could not be understood by the server 
- 404 Not Found: The requested resource cannot be found
- 500 Internal Server Error: The server encountered an unexpected condition
- 201 Resource Created
- 204 No Content found in case of delete.

### Sample JSON for User and Account
##### Customer Creation :
Endpoint : http://localhost:8080/v1/customers/add
```sh
{
  "contactDetailDto": {
    "emailId": "sonalithakare@gmail.com",
    "mobile": "+971589002023",
    "phone": "971589002023"
  },
   "customerAddressDtos": [
    {
      "address1": "Flat No :609",
      "address2": "BMW Bulding, DSO",
      "city": "Dubai",
      "country": "UAE",
      "state": "Dubai",
      "zip": "ABC"
    }
  ],
  "firstName": "Sonali",
  "lastName": "Thakare",
  "dob":"1989-01-27",
  "status": "ACTIVE",
  "customerNumber": 2
}

```
##### Create Account: : 
Endpoint : http://localhost:8080/v1/accounts/add
```sh
{
  "accountBalance": 1000,
  "accountNumber": 2,
  "accountStatus": "ACTIVE",
  "accountType": "SAVING",
  "bankDetailDto": {
    "branchAddress": {
      "address1": "Flat No :622",
      "address2": "Arabian Gate, DSO",
      "city": "Dubai",
      "country": "UAE",
      "state": "Dubai",
      "zip": "ABC"
    },
    "branchCode": "ADCB-1028",
    "branchName": "ADCB BANK "
  }
}
```

#### Link User Account To Customer ID:
Endpoint :http://localhost:8080/v1/accounts/1/link/customer/2
```sh
{  
   "currencyCode":"EUR",
   "amount":100000.0000,
   "fromAccountId":1,
   "toAccountId":2
}
```

#### Tramsfer Amount Between Accounts:
Endpoint : http://localhost:8080/v1/accounts/transfer/1
```sh
{
  "toAccountNumber": 2,
  "transferAmount": 100.50
}
```

## View Transactions for Account :

Endpoint : http://localhost:8080/v1/accounts/transactions/1

## View Account Details / Balance:
Endpoint :  http://localhost:8080/v1/accounts/2


## Current Scope :
- Implement Add/Update/Get/Delete/List Customers related endpoints
- Implement Add/Update/Get/Delete/List Accounts related endpoints
- Implement Link Account to Customers related endpoints
- Implement Transfer payment between accounts
- Implement View Account Transaction Endpoints
- Handle Concurrency Using (Not Good Practice)
- Exception and Error Handling 
- H2 Database In Memory
- Junit Test Case 
- Code Coverage using Jacoco Plugin
- Dockerized Application

## Future Scope (Not Considering now because of Time Limit: Need to considered ):
- Concurrency Using Optimistic (Database row locking)
- Advance Data Validation
- Advance Banking Business Rule Implementation
- Security to Rest Endpoint 
- Add Liquibase for Database Migration 
- Add Checkstyle to mantain Coding standard 
- Currency And Conversion Rates
- Proper Documentation (Help.md file )
