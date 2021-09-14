# Webflux + security + reactive h2 in file

### Run:
`mvn spring-boot:run`

### H2
https://127.0.0.1:8081  
JDBC URL: jdbc:h2:./data/auth  
DB Credentials in application.properties  

### Create user(not protected by permissions)
```
POST:http://localhost:8080/api/users/save
BODY:{"username": "user",
"password": 123456}```
```
`curl -X POST -i -H "Content-Type: application/json" --data "{\"username\":\"user\",\"password\":\"123456\"}" http://127.0.0.1:8080/api/users/save`
```
POST:http://localhost:8080/api/role/save
BODY:{"userId": 1,"roleId":2}
```
`curl -X POST -i -H "Content-Type: application/json" --data "{\"userId\":1,\"roleId\":2}" http://127.0.0.1:8080/api/role/save`