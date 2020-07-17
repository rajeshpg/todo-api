

start mongodb `docker run --name todo -d -p 27017:27017 mongo`

start the app
`sbt run`

login
`curl --location --request POST 'localhost:9000/auth' \
 --header 'Content-Type: application/json' \
 --data-raw '{"username":"rajesh", "password":"secret"}'`

 create todo
 `curl --location --request POST 'localhost:9000/todos' \
 --header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjp7InVzZXJOYW1lIjoicmFqZXNoIn19.M7xvPvMLk50mzXt303RRjKYGf8uSF1a-lr4eP5jcua4' \
 --header 'Content-Type: application/json' \
 --data-raw '{
     "description": "this is a test",
     "userName": "rajesh",
     "targetDate": "2020-07-18T18:30:32.150541Z"
 }'`

 get todos of a user
 `curl --location --request GET 'localhost:9000/user/todos' \
  --header 'Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjp7InVzZXJOYW1lIjoicmFqZXNoIn19.M7xvPvMLk50mzXt303RRjKYGf8uSF1a-lr4eP5jcua4'`
