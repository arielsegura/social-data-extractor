package io.github.arielsegura.socialpersistor.repositories;

import io.github.arielsegura.socialpersistor.domain.SearchTopicResult;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchTopicResultRepository extends ElasticsearchRepository<SearchTopicResult, String> {
}
