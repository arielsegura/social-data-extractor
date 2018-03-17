package io.github.arielsegura.socialportal.controllers;

import io.github.arielsegura.socialextractor.model.RestTopicResult;
import io.github.arielsegura.socialextractor.model.SearchRequest;
import io.github.arielsegura.socialportal.services.QuerySearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class SearchHomeController {

    private final ConnectionRepository connectionRepository;
    private final Twitter twitter;
    private final QuerySearchService querySearchService;

    @Autowired
    public SearchHomeController(ConnectionRepository connectionRepository, Twitter twitter, QuerySearchService querySearchService) {
        this.connectionRepository = connectionRepository;
        this.twitter = twitter;
        this.querySearchService = querySearchService;
    }

    @RequestMapping(method= RequestMethod.GET)
    public String searchUI(Model model, @RequestParam(value = "searchTerm", required = false) String searchTerm, @RequestParam(value = "justRecents", required = false, defaultValue = "true") boolean justRecents) {

        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "redirect:/connect/twitter";
        }

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setSearchTerm(searchTerm);
        searchRequest.setJustRecents(justRecents);

        List<RestTopicResult> topicResults = new ArrayList<>();
        if(!StringUtils.isEmpty(searchTerm)) {
            topicResults = querySearchService.search(searchRequest);
        }

        model.addAttribute(twitter.userOperations().getUserProfile());
        model.addAttribute("topicResults", topicResults);

        return "search";
    }

    @RequestMapping(method= RequestMethod.GET, path = "words")
    public String wordsUI(Model model, @RequestParam(value = "searchTerm", required = false) String searchTerm,
                          @RequestParam(value = "justRecents", required = false, defaultValue = "true") boolean justRecents,
                            @RequestParam(value = "refresh", required = false, defaultValue = "false") boolean refresh) {

        if (connectionRepository.findPrimaryConnection(Twitter.class) == null) {
            return "redirect:/connect/twitter";
        }

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setSearchTerm(searchTerm);
        searchRequest.setJustRecents(justRecents);
        searchRequest.setRefresh(refresh);

        List<RestTopicResult> topicResults = new ArrayList<>();
        if(!StringUtils.isEmpty(searchTerm)) {
            topicResults = querySearchService.search(searchRequest);
        }

        model.addAttribute(twitter.userOperations().getUserProfile());
        List<String> strings = topicResults.parallelStream()
                .map(RestTopicResult::getText)
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .collect(Collectors.toList());
        model.addAttribute("words", StringUtils.replaceIgnoreCase(String.join(" ", strings), searchTerm, ""));

        return "words";
    }

    @RequestMapping(method= RequestMethod.GET, path = "api/search")
    public @ResponseBody List<RestTopicResult> search(@RequestParam(value = "searchTerm", required = false) String searchTerm,
                                                      @RequestParam(value = "justRecents", required = false, defaultValue = "true") boolean justRecents,
                                                      @RequestParam(value = "refresh", required = false, defaultValue = "false") boolean refresh) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setSearchTerm(searchTerm);
        searchRequest.setJustRecents(justRecents);
        searchRequest.setRefresh(refresh);

        List<RestTopicResult> topicResults = new ArrayList<>();
        if(!StringUtils.isEmpty(searchTerm)) {
            topicResults = querySearchService.search(searchRequest);
        }

        return topicResults;
    }


}
