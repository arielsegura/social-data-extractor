package io.github.arielsegura.socialportal.clients;

import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.github.arielsegura.socialextractor.model.RestTopicResult;

import java.util.List;

public interface PersistorClient {

    @RequestLine("POST /topicresults?searchTerm={searchTerm}&searchUserId={searchUserId}")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    void store(@Param("searchTerm") String searchTerm, @Param("searchUserId") String searchUserId, List<RestTopicResult> restTopicResultList);

    @RequestLine("GET /topicresults?page={pageNumber}&size={pageSize}&searchTerm={searchTerm}&searchUserId={searchUserId}")
    @Headers({
            "Accept: application/json"
    })
    List<RestTopicResult> getTopicResults(@Param("pageNumber") int pageNumber, @Param("pageSize") int pageSize, @Param("searchTerm") String searchTerm, @Param("searchUserId") String searchUserId);

    static PersistorClient connect() {
        return Feign.builder()
                .logLevel(Logger.Level.FULL)
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .target(PersistorClient.class, "http://localhost:8081");
    }
}
