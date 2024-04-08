# Social Media Data Processor

A versatile modularized application that can be configured to fetch data from any API, process that data, and make it available for searching.

The application has been implemented in such a way that allows changing the source of Data and the way it is processed with minimal changes to the source code.

The application has been divided in 5 loosely coupled Java SpringBoot applications communicating through messaging queues or REST API calls only.

## ISSUES TO CHECK

- Problem in fetching data from elasticsearch since an Interface cannot be instantiated.
  - Convert to a single concrete class ???

## Component Applications

Below given is a brief overview of all loosely coupled applications in the project.

### DataProducer

`DataProducer` produces data by fetching it from a particular API. The data is pushed on to a kafka topic in its raw form or with minimal processing.

- The data to be pushed is encapsulated by wrapping it in a class named `KafkaDataObject` class. The class implements _Serializable_ interface and is parsed via **JsonSerializer** to be sent to Kafka topic.
- The `KafkaProducerConfig.java` contains a mapping of _**KafkaDataObject**_ to its corresponding class in `DataProducer`.
- The messages are mocked by a `MockDataFetcher` class which runs via the `CommandLineRunner.run` method.

### RegexManager

- `RegexManager` is a REST service to perform _CRUD_ operations on a NoSQL (_MongoDB_) database storing regex patterns. These regex patterns are used to filter and process the data.
- The current implementation consists of a simple _CRD - Create-Read-Delete -_ endpoints for regex patterns corresponding to **_Tumblr Blogs and Posts_**. The _**Rest Controller**_ has been implemented in way that allows easy duplication to create endpoints for data from other APIs as well without disturbing the existing endpoints.
- `TumblrPattern` class for defining instances of regex expression and mapping to corresponding collection in database, `TumblrPatternRepository` for handling database reads and writes, and `TumblrRegexController` to define corresponding endpoints.

### DataConsumer

- `DataConsumer` takes the raw data from kafka topic and the regex patterns from `RegexManager` to generated processed data. The regex patterns are used to find "interesting" data and label it. The processed data is passed to another kafka topic.
- The data received is mapped to `org.VersatileDataProcessor.DataProducer.models.KafkaDataObject` to allow deserialization of the Kafka message via `JsonDeserializer`. The mappings are present in `KafkaConsumerConfig.java`.
- A specialized `ConsumerFactory` and `ContainerFactory` are defined dedicated to `KafkaDataObject` class in `KafkaProducerConfig.java`.
- A dedicated Listener defined in `KafkaConsumerService.java` uses this container-factory to listen for kafka messages containing `KafkaDataObject` instances.

### ElasticsearchWriter

`ElasticsearchWriter` takes the processed data from the kafka topic and writes it to the elastic search database.

Decoupling database writes from data processing allows one to easily switch the database in future if needed without the need to modify the way data is processed.

### SearchPoint

`SearchPoint` is a simple Web Application API that allows performing search operations on the data present in elasticsearch. The `SearchPoint` application is the only application that needs to exposed to external world.


## Steps to integrate a new API

> Follow the step-by-step guide to add a new API to fetch data from.

### Data Producer

1. In `DataProducer`, go to `models/MessageType.java` Enum definition and add a new Enum type corresponding to the new API.
2. Go to `models/apiMessages` folder and define a new Class implementing `ApiMessageInterface` interface and define the attributes to be fetched from the API.
   1. The class is supposed to be template for the JSON response returned by the API.
   2. Make sure to use matching Data types and names as returned in the JSON response.
   3. Keep the variables as private and define appropriate Getters, Setters and Constructors using `Lombok`.
3. In `config/ApiMessageProducerConfig.java`, add the Type-Mapping to the JsonSerializer in a manner similar to other messages.
   1. NO NEED TO FOLLOW THIS STEP ANYMORE. THE TYPE MAPPINGS ARE DYNAMICALLY GENERATED VIA REFLECTIONS API AND SO ANY CLASS THAT EXTENDS THE `ApiMessageInterface` WILL AUTOMATICALLY BE INCLUDED IN THE TYPE MAPPING DURING RUN-TIME.
4. Go to `fetcher` folder and define a new `DataFetcherInterface` interface implementation that fetches data from the new API.
   1. Make sure to use appropriate params, and tokens wherever needed.
   2. DO NOT HARD-CODE ANY API TOKENS. USE THE `.env` AND `src/main/resources/application.properties` FILES TO IMPORT THE API KEYS/SECRETS.

### Regex Manager

### Data Consumer

> The Steps are almost identical to the steps in DataProducer microservice

1. In `DataConsumer`, go to `models/MessageType.java` and add the new Enum type corresponding to the new API.
   1. Make sure that the enum type defined in `DataProducer` and in `DataConsumer` is identical.
2. Go to `models/apiMessages` folder and copy-paste the new class implementing the `ApiMessageInterface` interface as defined in the `DataProducer`.
3. Move all the additional classes defined along side the **_public class_** to `models/messageSupport`.
   1. If multiple additional classes are defined, then group them together under a single a package inside `models/messageSupport`.
4. In `config/ApiMessageConsumeConfig.java`, add the Type-Mapping to the JsonDeserializer as done in `DataProducer Step 3`.
   1. SIMILAR TO `DataProducer`, THE TYPE MAPPINGS ARE DYNAMICALLY GENERATED AND SO THIS STEP IS KEPT FOR INFORMATION PURPOSES ONLY.
   2. Note that the left side of the `:` in the type mapping must be same for `DataProducer` and `DataConsumer` for the messages to be directed appropriately.
   3. The right side of the `:` may not have the same name as long as the definitions of the classes for Producer and Consumer is identical.
5. In `models/processedMessages`, define corresponding `MessageInterface` implementation.
   1. Make sure to define a static **_processMessage()_** function that converts the `ApiMessageInterface impl` to `MessageInterface impl`.
   2. Ensure use of `@JsonDeserialize(as = <processed-message-class>)` and `@TypeAlias("<simple-class-name>")` on the class definition.
6. In `deserializers/ApiMessageInterfaceDeserializer`, add appropriate `switch-case` to return the object as instance of the newly defined `ApiMessageInterface` implementation class when message type matches the new API.
7. In `services/ApiMessageConsumerService`, in `handleApiMessage()`, add switch case for the newly created API Message Type.

### Elasticsearch Database Manager

1. In `ElasticsearchWriter`, add the new enum defined in `models/MessageType.java`.
2. In `models/processedMessages`, copy the corresponding `MessageInterface` implementation class from `DataConsumer`.
   1. Make sure to define remove the static **_processMessage()_** function.
   2. Ensure use of `@JsonDeserialize(as = <processed-message-class>)` and `@TypeAlias("<simple-class-name>")` on the class definition.
   3. Also define `@Document(indexName = <index-to-store-data-in>)` to define the index in which the data of that class is to be stored.
      1. CURRENTLY ALL THE DATA IS STORED UNDER A SINGLE INDEX. THE INDEX NAME IS DEFINED IN CASE DATA IS TO SEPARATED IN THE FUTURE VERSIONS.
3. Also add any additional classes/packages defined in `models/essageSupport` from `DataConsumer` to the corresponding path in `ElasticsearchWriter`. 
4. Similar to `controller/ApiMessageInterfaceDeserializer` in `DataConsumer`, go to `controller/MessageInterfaceDeserializer` and define case for the newly defined Enum type.


### SearchPoint
