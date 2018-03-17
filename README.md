# Social Network Data Extractor

This project works as a data extractor for social networks.
Once you're logged in with the social network you can use either an API or a UI to query topics. 

## Motivation
This work has been done in the context of a postgraduate course about data intelligence on big data at the University of La Plata.
The idea is to build software that is able to store lot of data (GBs or TBs) with an acceptable performance. 

## How it was achieved
On this first version, the stack is as follows:  
- `Spring Data Portal`: It's the entry point used to log in with Twitter. The session is stored in `redis`. 
It orchestrates `Social Data Extractor` and `Social Data Persistor`. 
- `Social Data Extractor`: It's the connection to social networks. It's a `spring-boot` microservice that uses `spring-social` to interact with `Twitter`. 
It queries tweets and returns them in a `TopicResult` format without storing anything - it's all in memory. 
- `Social Data Persistor`: It's the persistence layer. It stores `TopicResults` in a NoSQL database (`Cassandra` in this case). 
It also stores the data on `ElasticSearch`, so we can search for results faster.    

The portal and the API by default gets entries from `ElasticSearch`. If needed, it's possible to pass a parameter to the API so it fetches new results about that topic. 


## Supported social networks
- Twitter 

## What you need to run
- Run `mvn clean install -DskipTests` -> tests pass only if you have cassandra, redis and elasticsearch running. 
- `Spring Data Portal`: Pass a system property `twitter.appsecret` with your app secret. 
- `Spring Data Extractor`: Pass a system property `twitter.appsecret` with your app secret.
- Launch a `Cassandra` instance 
- `Spring Data Persistor`: Point to your `Cassandra` instance
- Launch a `Redis` instance
- `Run all the services` 

## Commands
1. Start elasticsearch: `docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.2.2`
2. Start cassandra: `docker run --name cassandra-container -p 9042:9042 -p 9160:9160 -e CASSANDRA_START_RPC=true -d cassandra`
3. Start redis: `docker run -d redis`
4. Replace changeme app secret for twitter with your app secret.
5. `mvn clean install`
6. `java -jar social-data-persistor-service/target/social-data-persistor-service.jar`
7. `java -jar social-data-portal/target/social-data-portal.jar`
8. `java -jar social-data-extractor-service/target/social-data-extractor-service.jar`
9. In chrome -> `http://localhost:8080/words?searchTerm=lollapalooza`
10. Enjoy!

https://stackoverflow.com/questions/26909408/export-cassandra-query-result-to-a-csv-file

## Known issues
- The session stored in redis works also with browsers without a session. 
This means that once someone logged in with a social network, all the users are going to work with that session. 


## Improvements
Add streaming support
