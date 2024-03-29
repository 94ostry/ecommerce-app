# ecommerce-app
eCommerce test app

### Build & Run
`gradlew build && java -jar build\libs\ecommerce-app-1.0.0..jar` 

### Additional Links
These additional references to services in application:

* [H2 console](http://localhost:8080/h2-console)
* [Swagger](http://localhost:8080/swagger-ui.html) REST API documentation.

### How add authentication to the service?
As a option of authentication of service I would consider Token-Based Authentication System, e.g. JWT.
Advantages of JWT:
- No Session to Manage (stateless): The JWT is a self contained token which has authetication  information, 
expire time information, and other user defined claims digitally signed.
- A single token can be used with multiple services. 
- Good Performance because reduce the network round trip time (much less traffic than a session-based solution).
- Decoupled/Decentralized: The token can be generated anywhere.


### How make the service redundant?

If I were to make this website redundant in the first place I would change the way it is delivered.
Instead of providing a JAR file, the result of the code building process would be a `docker image`. 
Then, using Kubernetes this image would be use to build services and pods with replica set bigger than one.

 Of course, the next step would be to change the database. In-memory H2 should be replaced with some other 
 scalable relational base. Thanks to this, the list of products will be in one place and will be available 
 to all services. For optimization, use read-only db nodes.
 
 The model (entities) itself also needs to be changed. A `version` column should be added to the `Product` entity to prevent 
 simultaneous modification of the same entity, e.g. by changing the price by two simultaneous transactions.