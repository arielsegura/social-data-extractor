package io.github.arielsegura.socialportal.clients;

import feign.*;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.github.arielsegura.socialextractor.model.RestTopicResult;
import io.github.arielsegura.socialextractor.model.SearchRequest;

import java.util.List;

public interface SearchClient {

    @RequestLine("POST /?searchUserId={searchUserId}")
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    List<RestTopicResult> performSearch(SearchRequest searchRequest, @Param("searchUserId") String searchUserId);

    static SearchClient connect() {
        return Feign.builder()
                .logLevel(Logger.Level.FULL)
                .decoder(new JacksonDecoder())
                .encoder(new JacksonEncoder())
                .target(SearchClient.class, "http://localhost:8082");
    }
}
