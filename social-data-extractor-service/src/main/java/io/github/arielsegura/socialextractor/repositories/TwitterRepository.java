package io.github.arielsegura.socialextractor.repositories;

import io.github.arielsegura.socialextractor.model.SearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchParameters;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class TwitterRepository {

    static Logger logger = LoggerFactory.getLogger(TwitterRepository.class);

    private final Twitter twitter;

    @Autowired
    public TwitterRepository(Twitter twitter) {
        this.twitter = twitter;
    }

    public List<SearchResults> search(SearchRequest searchRequest){
        SearchParameters searchParameters = new SearchParameters(searchRequest.getSearchTerm());
        if(searchRequest.isJustRecents()){
            searchParameters.resultType(SearchParameters.ResultType.RECENT);
        }

        searchParameters.count(searchRequest.getCount());

        List<SearchResults> results = new ArrayList<>();

        SearchResults search = null;
        int count = 0;
        try {
            do {
                if (search != null) {
                    search.getTweets().parallelStream().map(tweet -> tweet.getCreatedAt()).sorted().findFirst().ifPresent(date -> {

                        searchParameters.until(date);
                    });
                }
                logger.info("Executing request {}", count ++ );
                search = twitter.searchOperations().search(searchParameters);
                results.add(search);
            } while (Objects.nonNull(search) && !search.isLastPage() && results.size() < searchRequest.getCount());
        } catch (Exception ex){
            ex.printStackTrace();
        }

        return results;
    }

    public String getUserId(){
        return twitter.userOperations().getProfileId() + "";
    }

}
