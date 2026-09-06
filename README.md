# Url shortener

Example implementation of a URL shortener service using Spring Boot 4.

## Instructions

Execute the application to start the webserver:

``
./gradle bootRun
``

Open a new terminal an execute a call:

``
curl --location 'http://localhost:8080/api/v1/generate-url' \
--header 'Content-Type: application/json' \
--data '{
  "originalUrl": "https://httpbin.org/uuid"
}'
``

The result must be something like this:

``
{
  "originalUrl": "https://httpbin.org/uuid",
  "shortenedUrl":"http://localhost:8080/Exx08rTxpN"
}
``

If you execute the same call again, you will get the same shortened URL.

To use the shortened URL, execute a call like this:

``
curl --location 'http://localhost:8080/Exx08rTxpN'
``

You will be redirected to the original URL, and the result will be something like this:

``
{
  "uuid": "f3e1c8d0-5b6a-4c9e-9f1b-2d3e4f5a6b7c"
}
``

To check the values stored in the database, you can access using the console at:

http://localhost:8080/h2-console <br />
username: test <br />
password: test <br />
