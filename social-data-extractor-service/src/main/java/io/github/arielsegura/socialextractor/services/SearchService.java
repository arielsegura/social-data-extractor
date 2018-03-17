package io.github.arielsegura.socialextractor.services;

import io.github.arielsegura.socialextractor.model.RestTopicResult;
import io.github.arielsegura.socialextractor.model.SearchRequest;
import io.github.arielsegura.socialextractor.repositories.TwitterRepository;
import io.github.arielsegura.socialextractor.utils.MappingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final TwitterRepository twitterRepository;

    static Logger logger = LoggerFactory.getLogger(SearchService.class);

    public SearchService(TwitterRepository twitterRepository) {
        this.twitterRepository = twitterRepository;
    }

    public List<RestTopicResult> search(SearchRequest searchRequest){

        logger.info("Getting elements from twitter");

        List<RestTopicResult> allByMainSearchEquals = Collections.emptyList(); //TODO extract from redis?
        if(allByMainSearchEquals.isEmpty()) {

            List<SearchResults> searchResults = twitterRepository.search(searchRequest);
            String userId = twitterRepository.getUserId();
            List<Tweet> tweets = searchResults.parallelStream()
                    .flatMap(result -> result.getTweets().stream())
                    .collect(Collectors.toList());

            List<RestTopicResult> restTopicResults = tweets
                    .parallelStream()
                    .map(tweet -> MappingUtils.toTopicResult(searchRequest.getSearchTerm(), userId, tweet))
                    .collect(Collectors.toList());
            //TODO store value in cache?
            logger.info("Done");
            return restTopicResults;
        } else {
            return allByMainSearchEquals;
        }
    }

}
