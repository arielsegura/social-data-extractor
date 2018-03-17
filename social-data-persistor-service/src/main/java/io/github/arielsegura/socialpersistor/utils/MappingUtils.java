package io.github.arielsegura.socialpersistor.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.arielsegura.socialextractor.model.RestTopicResult;
import io.github.arielsegura.socialpersistor.domain.SearchTopicResult;
import io.github.arielsegura.socialpersistor.domain.TopicResult;

public class MappingUtils {

    public static TopicResult fromRestTopicResult(RestTopicResult restTopicResult){
        return new ObjectMapper().convertValue(restTopicResult, TopicResult.class);
    }

    public static RestTopicResult fromTopicResult(TopicResult topicResult){
        return new ObjectMapper().convertValue(topicResult, RestTopicResult.class);
    }

    public static RestTopicResult fromSearchTopicResult(SearchTopicResult searchTopicResult) {
        return new ObjectMapper().convertValue(searchTopicResult, RestTopicResult.class);
    }

    public static SearchTopicResult buildSearchTopicResult(TopicResult topicResult) {
        return new ObjectMapper().convertValue(topicResult, SearchTopicResult.class);
    }
}
