package io.github.arielsegura.socialpersistor.repositories;

import io.github.arielsegura.socialpersistor.Application;
import io.github.arielsegura.socialpersistor.domain.SearchTopicResult;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.UUID;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        Application.class
})
@EnableCassandraRepositories(basePackageClasses = TopicResultRepository.class)
@EnableElasticsearchRepositories(basePackageClasses = SearchTopicResultRepository.class)
public class SearchTopicResultRepositoryTest {

    @BeforeClass
    public static void setUp() throws Exception {
        System.setProperty("es.set.netty.runtime.available.processors", "false"); //https://github.com/netty/netty/issues/6956
    }

    @Autowired
    SearchTopicResultRepository searchTopicResultRepository;

    @Test
    public void simpleSaveTest() throws Exception {
        String id = UUID.randomUUID() + "";
        SearchTopicResult topicResult = new SearchTopicResult(id, "my-text", new Date(), "my-search", "myuserid");
        SearchTopicResult save = searchTopicResultRepository.save(topicResult);
        SearchTopicResult one = searchTopicResultRepository.findById(id).get();
        assertEquals(topicResult, one);
    }

    @Test
    public void searchByMainSearchAndUser() throws Exception {
        String id = UUID.randomUUID() + "";
        String mainSearch = "my-search";
        String myuserid = "myuserid";
        SearchTopicResult topicResult = new SearchTopicResult(id, "my-text", new Date(), mainSearch, myuserid);
        searchTopicResultRepository.save(topicResult);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("mainSearch", mainSearch))
                .withQuery(matchQuery("searchUserId", myuserid))
                .build();
        SearchTopicResult one = searchTopicResultRepository.search(searchQuery).getContent().get(0);
        assertEquals(topicResult, one);
    }

    @Test
    public void saveTwice() throws Exception {
        String id = UUID.randomUUID() + "";
        String mainSearch = "my-search";
        String myuserid = "myuserid";
        SearchTopicResult topicResult = new SearchTopicResult(id, "my-text", new Date(), mainSearch, myuserid);
        searchTopicResultRepository.save(topicResult);
        searchTopicResultRepository.save(topicResult);
    }
}
