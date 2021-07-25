# User Service Application

<br/>

This is a sample application related to User (create/update/read/delete)
<br/>

# Features

  - Create a user
  - Update a user
  - Delete a user
  - Fetch users based on filter and sorting
<br/>

### Technical Stack
- Spring Boot
- MariaDB
- Flyway Migration
- Java 8
<br/>

### Prerequisite
- MariaDB

### Installation

user-service requires Java 8 to run.

Build the source code

```sh
$ mvn clean package
```
Run the user-service

```sh
java -jar xxxxxxxxx.jar
```
<br/>


## API Details & Description
Swagger-Ui

`http://localhost:9898/user-service/swagger-ui.html`

<br/>


#### Create User Request

Curl Request

``curl -X POST "http://localhost:9898/user-service/v1/users" -H "accept: application/v1+json" -H "Content-Type: application/json" -d "{ \"data\": { \"nationality\": \"Indian\" }, \"email\": \"mohit.jain@user.com\", \"mobile\": 9999888899, \"name\": \"Mohit Jain\", \"status\": \"ACTIVE\"}"
``

Response

```json
{
  "userId": 1,
  "name": "Mohit Jain",
  "mobile": "9999888899",
  "email": "mohit.jain@user.com",
  "status": "ACTIVE",
  "data": {
    "nationality": "Indian"
  },
  "created": "2021-07-25 16:06:27",
  "lastUpdated": "2021-07-25 16:06:27"
}
```
<br />

#### Get User Request

Curl Request

``curl -X GET "http://localhost:9898/user-service/v1/users/1" -H "accept: application/v1+json"
``

Response

```json
{
  "userId": 1,
  "name": "Mohit Jain",
  "mobile": "9999888899",
  "email": "mohit.jain@user.com",
  "status": "ACTIVE",
  "data": {
    "nationality": "Indian"
  },
  "created": "2021-07-25 16:06:27",
  "lastUpdated": "2021-07-25 16:06:27"
}
```

#### Update User Request

Curl Request

``curl -X PUT "http://localhost:9898/user-service/v1/users/1" -H "accept: application/v1+json" -H "Content-Type: application/json" -d "{ \"data\": { \"Gender\": \"Male\" }, \"email\": \"web@user.com\", \"mobile\": 9999888899, \"name\": \"Mohit Kumar Jain\", \"status\": \"ACTIVE\"}"
``

Response

```json
{
  "userId": 1,
  "name": "Mohit Kumar Jain",
  "mobile": "9999888899",
  "email": "web@user.com",
  "status": "ACTIVE",
  "data": {
    "Gender": "Male"
  },
  "created": "2021-07-25 16:06:27",
  "lastUpdated": "2021-07-25 16:09:05"
}

```

#### Get All User Request

Curl Request

``curl -X GET "http://localhost:9898/user-service/v1/users" -H "accept: application/v1+json"``

Response

```json
{
  "users": [
    {
      "userId": 1,
      "name": "Mohit Kumar Jain",
      "mobile": "9999888899",
      "email": "web@user.com",
      "status": "ACTIVE",
      "data": {
        "Gender": "Male"
      },
      "created": "2021-07-25 16:06:27",
      "lastUpdated": "2021-07-25 16:09:05"
    }
  ],
  "metaData": {
    "page": 1,
    "nextPage": -1,
    "pageSize": 30,
    "pageCount": 1,
    "totalCount": 1
  }
}
```

#### Delete User Request

Curl Request

``curl -X DELETE "http://localhost:9898/user-service/v1/users/1" -H "accept: application/v1+json"``

Response

```json
{
  "message": "User has been deleted successfully with userId 1"
}
```


<br/>
<br/>