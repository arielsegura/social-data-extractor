package io.github.arielsegura.socialpersistor.repositories;


import io.github.arielsegura.socialpersistor.domain.TopicResult;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicResultRepository extends CassandraRepository<TopicResult, String> {

    List<TopicResult> findAllByMainSearchEqualsAndSearchUserIdEquals(String mainSearch, String searchUserId);

}
