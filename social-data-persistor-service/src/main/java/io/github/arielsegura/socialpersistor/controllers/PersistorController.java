package io.github.arielsegura.socialpersistor.controllers;

import io.github.arielsegura.socialextractor.model.RestTopicResult;
import io.github.arielsegura.socialpersistor.domain.TopicResult;
import io.github.arielsegura.socialpersistor.repositories.SearchTopicResultRepository;
import io.github.arielsegura.socialpersistor.repositories.TopicResultRepository;
import io.github.arielsegura.socialpersistor.utils.MappingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/topicresults")
public class PersistorController {

    static Logger logger = LoggerFactory.getLogger(PersistorController.class);

    private final TopicResultRepository topicResultRepository;
    private final SearchTopicResultRepository searchTopicResultRepository;

    @Autowired
    public PersistorController(TopicResultRepository topicResultRepository, SearchTopicResultRepository searchTopicResultRepository) {
        this.topicResultRepository = topicResultRepository;
        this.searchTopicResultRepository = searchTopicResultRepository;
    }

    @RequestMapping(method= RequestMethod.POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void store(@RequestParam String searchTerm, @RequestParam String searchUserId, @RequestBody List<RestTopicResult> restTopicResults) {
        List<TopicResult> items = restTopicResults.parallelStream()
                .map(MappingUtils::fromRestTopicResult)
                .collect(Collectors.toList());
        logger.info("Storing items in cassandra");
        topicResultRepository.saveAll(items);
        logger.info("Indexing items in elasticsearch");
        searchTopicResultRepository.saveAll(items.parallelStream().map(MappingUtils::buildSearchTopicResult).collect(Collectors.toList()));
    }

    @RequestMapping(method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    //https://stackoverflow.com/questions/18813615/how-to-avoid-the-circular-view-path-exception-with-spring-mvc-test/27797981
    public @ResponseBody List<RestTopicResult> getTopicResults(@RequestParam String searchTerm, @RequestParam String searchUserId, Pageable pageable){
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("mainSearch", searchTerm))
                .withQuery(matchQuery("searchUserId", searchUserId))
                .build();
        logger.info("Returning items from elasticsearch");
        try {
            return searchTopicResultRepository.search(searchQuery)
                    .stream()
                    .map(MappingUtils::fromSearchTopicResult)
                    .collect(Collectors.toList());
        } catch (Exception ex){
            logger.warn("There was an error while fetching data from elastic", ex);
            return Collections.emptyList();
        }
    }


}
