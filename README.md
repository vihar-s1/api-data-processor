# Social Media Data Processor

A versatile modularized application that can be configured to fetch data from any API, process that data, and make it available for searching.

The application has been implemented in such a way that allows changing the source of Data and the way it is processed with minimal changes to the source code.

The application has been divided in 5 loosely coupled Java SpringBoot applications communicating through messaging queues or REST API calls only.

## Component Applications

Below given is a brief overview of all loosely coupled applications in the project.

### DataProducer

`DataProducer` produces data by fetching it from a particular API. The data is pushed on to a kafka topic in its raw form or with minimal processing.

### RegexManager

`RegexManager` is a REST service to perform CRUD operations on a NoSQL (_MongoDB_) database storing regex patterns. These regex patterns are used to filter and process the data.

### DataConsumer

`DataConsumer` takes the raw data from kafka topic and the regex patterns from `RegexManager` to generated processed data. The regex patterns are used to find "interesting" data and label it. The processed data is passed to another kafka topic.

### ElasticsearchWriter

`ElasticsearchWriter` takes the processed data from the kafka topic and writes it to the elastic search database.

Decoupling database writes from data processing allows one to easily switch the database in future if needed without the need to modify the way data is processed.

### SearchPoint

`SearchPoint` is a simple Web Application API that allows performing search operations on the data present in elasticsearch. The `SearchPoint` application is the only application that needs to exposed to external world.
