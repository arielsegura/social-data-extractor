package io.github.arielsegura.socialextractor.controllers;

import io.github.arielsegura.socialextractor.model.RestTopicResult;
import io.github.arielsegura.socialextractor.model.SearchRequest;
import io.github.arielsegura.socialextractor.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/")
public class SearchController {

    private final SearchService searchService;
    private final ConnectionRepository connectionRepository;
    private final Twitter twitter;

    @Autowired
    public SearchController(SearchService searchService, Twitter twitter, ConnectionRepository connectionRepository) {
        this.searchService = searchService;
        this.connectionRepository = connectionRepository;
        this.twitter = twitter;
    }

    @RequestMapping(method= RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<RestTopicResult> search(@RequestBody SearchRequest searchRequest) {
        return searchService.search(searchRequest);
    }

}
