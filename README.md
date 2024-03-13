# Social Media Data Processor

A versatile modularized application that can be configured to fetch data from any API, process that data, and make it available for searching.

The application has been implemented in such a way that allows changing the source of Data and the way it is processed with minimal changes to the source code.

The application has been divided in 5 loosely coupled Java SpringBoot applications communicating through messaging queues or REST API calls only.

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
