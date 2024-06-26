# API Data Processor

- A modularized SpringBoot Micro-service based application that provides a central database consisting of posts from various social-media applications.
- The 5 microservices are loosely coupled and can be run independently, while communicating with each other using Kafka, and HTTP calls.
- A 6th Springboot application `infra` acts as a library to provide common configurations and utilities to the other microservices.

| Modules              | Version | Description                                                              |
|----------------------|---------|--------------------------------------------------------------------------|
| producer             | 2.0.0   | Fetches data from various social media APIs and sends it to Kafka.       |
| consumer             | 2.0.0   | Reads data from Kafka and processes it.                                  |
| elasticsearchManager | 2.0.0   | Manages the elasticsearch database and writes data to it.                |
| searchService        | 2.0.0   | Provides a REST API to search data in the elasticsearch database.        |
| regexManager         | 1.0.0   | Manages the regular expressions used in processing the API responses.    |
| infra                | 2.0.0   | Provides common configurations and utilities to the other microservices. |

## Table of Contents

- [API Data Processor](#api-data-processor)
  - [Table of Contents](#table-of-contents)
  - [Microservices](#microservices)
    - [producer](#producer)
    - [consumer](#consumer)
    - [elasticsearchManager](#elasticsearchmanager)
    - [searchService](#searchservice)
    - [regexManager](#regexmanager)
  - [Running The Project](#running-the-project)
  - [Steps to integrate a new API](#steps-to-integrate-a-new-api)
    - [infra](#infra)
    - [producer](#producer-1)
    - [consumer](#consumer-1)

## Microservices

Below given is a brief overview of all loosely coupled applications in the project.

### producer

- `producer` is a REST service that fetches data from various social media APIs and sends it to a Kafka topic. 
- The data is fetched using the `service.api` package and the data is sent to Kafka using the `KafkaProducerService` class.
- The `KafkaProducerConfig` class defines the Kafka Producer configurations and the `KafkaProducerService` class uses the `KafkaTemplate` to send the data to the Kafka topic.
- `ApiService` abstract class defines the template code required for fetching data from the API. The implementation classes are called periodically to fetch data from the API.
  - `NOTE` the implementation classes are in `service.api` package and each must be annotated with `@Service` to be picked up by the SpringBoot Application Context.

#### Case of Reddit API

Reddit API requires multiple API calls in order to authenticate and fetch data.
- First, a `POST` request is made to https://www.reddit.com/api/v1/authorize to get the authentication code.
  - It takes following parameters:
    - `client_id` : The client id of the application.
    - `response_type` : The response type. In this case, it is `code`.
    - `state` : A random string to prevent CSRF attacks.
    - `redirect_uri` : The redirect URI of the application, in this case `http://localhost:8082/reddit/callback`.
    - `duration` : The duration for which the token is valid. In this case, it is `permanent`.
    - `scope` : The scope of the token. In this case, it is `read`.
- The authorize API call then makes `GET` call to the `redirect_uri` with parameters `code` and `state` or `error` in case of error.
- The `redirect_uri` endpoint uses the code to send a `POST` request to https://www.reddit.com/api/v1/access_token to get the access token.
  - It takes following parameters:
    - `grant_type` : The grant type. In this case, it is `authorization_code`.
    - `code` : The code received from the authorize API call.
    - `redirect_uri` : The redirect URI of the application, in this case `http://localhost:8082/reddit/callback`.
  - It returns `access_token` and `refresh_token` on success which are stored in `RedditService` as Instance Variables.
  - The `access_token` is used to make further API calls to fetch data.
  - The `refresh_token` is used to get a new `access_token` when the current one expires.

### consumer

- `consumer` is a Kafka Consumer that reads data from the Kafka topic and processes it.
- A dedicated listener is defined in `KafkaConsumerService` class which listens to the Kafka topic and processes the data.
- It receives all the api responses and adapts them to a common class named `GenericChannelPost`.
  - Since the apiResponse can contain multiple posts, the adapter returns `List<GenericChannelPost>`.
- Each of these posts is then sent to the `elasticsearchManager` microservice using an asynchronous REST call.

### elasticsearchManager

- `elasticsearchManager` receives the data from the `consumer` microservice and stores it in the elasticsearch database.
- The manager focuses on the WRITE operations to the elasticsearch database.
- The purpose of the `elasticsearchManager` is to decouple the database from the process of generating data.
- With this, another database can easily replace elasticsearch and the central post repository since the consumer does not require knowledge of database internals to send a POST request to add a new post.
 
### searchService

- `searchService` is a simple Web Application API that allows performing search operations on the data present in elasticsearch. The `searchService` application is the only application that needs to exposed to external world.
- This allows restricting access to the public to READ-only operations preventing any unauthorized modifications to the data.
- The `searchService` can be merged with the `elasticsearchManager` application to provide a single point of access to the data.
  - RBAC will have to be implemented in that case with `elasticsearchManager` operations having WRITE access and `searchService` operations having READ access.

### regexManager

- `regexManager` is a REST service with CRUD operations to manage the regular expressions useful in processing API responses.
- This microservice is an additional feature that can help pre-process the posts and introduce additional tags and filters to the posts.
- Although implemented, the regexManager is `not upto-date` with the latest version of the project and so, is `not used` in the current version.

## Running The Project

- The Project is a Springboot project requiring `Gradle` and `Java-21`.
- Clone The repository on your machine using

```commandline
git clone https://github.com/vihar-s1/api-data-processor.git
```

- The project is divided into 5 microservices and 1 library.
- Each microservice requires a separate environment file to run.
- The environment file is named `.env` and is present in the root directory of each microservice.
- Use `.env.sample` in each microservice to create a new `.env` file.
- Once the `.env` file is created, run the following command from project root to start the microservice.

```commandline
gradle producer:bootRun
``` 
```commandline
gradle consumer:bootRun
``` 
```commandline
gradle elasticsearchManager:bootRun
``` 
```commandline
gradle searchService:bootRun
``` 
```commandline
gradle regexManager:bootRun
```

## Steps to integrate a new API

> Follow the step-by-step guide to add a new API to fetch data from.

### infra

- Add a new Enum in `models.ApiType` to represent the new API.
- Create a new class in the `models.apiResponse` package in the that represents the response from the API.
- Create appropriate class for the posts as well.
- The response must implement `ApiResponseInterface`.
- Go to `models.deserializer.ApiResponseInterfaceDeserializer` and add a new case for the newly created API enum for successful deserialization.
- In the `models.genericChannelPost.Adapter`, implement a static function named `toGenericChannelPost` that converts the API response to a `GenericChannelPost`.

### producer

- Create a new class in the `producer.service.api` package that extends abstract class `ApiService`.
  - The class must be annotated with `@Service`.
  - The class must implement the `fetchData` method to fetch data from the API along with the additional abstract methods.
- Add any additional configurations secrets required in the `application.properties` file.
  - This may include API key, secret, token or more.
  - Make sure to import them in `application.properties` from `.env` file.
  - Use `@Value` annotation to inject the values in the class.
  - Add import validations in the `isExecutable` method to check if the required configurations are present.

### consumer

- In `services.KafkaConsumerService`, add a new case for the new API in the `genericApiResponseListener` method.

That's It !! You have successfully integrated a new API to fetch data from.