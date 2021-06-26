# SAFECOMM

### Set Up:

Add a `/config` folder at the root of the project, and add a file to it named `application.properties`

Update this file with SMTP information, and other environment variables.

### Start Up:

If you are performing CRUD operations, make sure you have started the mongod server.

open terminal
`sudo mongod`

open Mongo Explorer, or MongoDB Compass, and make a connection to the mongo server.

### Add Block
POST http://localhost:8080/addBlock
Content-Type: application/json

{"blockData":"210 YaboCoins"}

<> 2017-12-29T013518.200.json

###
