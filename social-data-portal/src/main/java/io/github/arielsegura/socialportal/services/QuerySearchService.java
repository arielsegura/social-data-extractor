package io.github.arielsegura.socialportal.services;

import io.github.arielsegura.socialextractor.model.RestTopicResult;
import io.github.arielsegura.socialextractor.model.SearchRequest;
import io.github.arielsegura.socialportal.clients.PersistorClient;
import io.github.arielsegura.socialportal.clients.SearchClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

@Service
public class QuerySearchService {

    final PersistorClient persistorClient;

    final Twitter twitter;

    final SearchClient searchClient;

    @Autowired
    public QuerySearchService(PersistorClient persistorClient, Twitter twitter, SearchClient searchClient) {
        this.persistorClient = persistorClient;
        this.twitter = twitter;
        this.searchClient = searchClient;
    }

    public List<RestTopicResult> search(SearchRequest searchRequest) {
        String userId = twitter.userOperations().getProfileId() + "";
        List<RestTopicResult> topicResults = persistorClient.getTopicResults(0, 50, searchRequest.getSearchTerm(), userId);
        Supplier<List<RestTopicResult>> searchSupplier = () -> {
            List<RestTopicResult> restTopicResults = searchClient.performSearch(searchRequest, userId);
            try {
                persistorClient.store(searchRequest.getSearchTerm(), userId, restTopicResults);
            } catch (Exception ex){
                ex.printStackTrace();
            } finally {
                return restTopicResults;
            }
        };

        if(topicResults.isEmpty()){
            return searchSupplier.get();
        } else if(searchRequest.isRefresh()){
            topicResults.addAll(searchSupplier.get());
            return topicResults;
        } else {
            return topicResults;
        }
    }
}
