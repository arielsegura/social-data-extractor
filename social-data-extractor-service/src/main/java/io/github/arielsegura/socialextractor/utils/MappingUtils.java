package io.github.arielsegura.socialextractor.utils;

import io.github.arielsegura.socialextractor.model.RestTopicResult;
import org.springframework.social.twitter.api.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public final class MappingUtils {

    public static RestTopicResult toTopicResult(String searchTerm, String userId, Tweet tweet) {
        RestTopicResult topicResult = new RestTopicResult(tweet.getId() + "", tweet.getText(), tweet.getCreatedAt(), searchTerm);
        topicResult.setFavorite(tweet.isFavorited());
        topicResult.setCreatedAt(tweet.getCreatedAt());
        topicResult.setId(tweet.getId() + "");
        topicResult.setText(tweet.getText());
        topicResult.setSearchUserId(userId);
        topicResult.setFavoriteCount(tweet.getFavoriteCount());
        topicResult.setFromUser(tweet.getFromUser());
        topicResult.setFromUserId(tweet.getFromUserId() + "");
        topicResult.setHashTags(processHashTags(tweet));
        topicResult.setMediaUrl(processURLs(tweet));
        topicResult.setMentions(processMentions(tweet));
        topicResult.setOriginalId(tweet.getIdStr());
        topicResult.setProfileImageUrl(tweet.getProfileImageUrl());
        topicResult.setShared(tweet.isRetweeted());
        topicResult.setShareCount(tweet.getRetweetCount());
        topicResult.setIsAlreadyShared(tweet.isRetweet());
        return topicResult;
    }

    private static String processMentions(Tweet tweet) {
        return Optional.ofNullable(tweet.getEntities())
                .map(Entities::getMentions)
                .map(List::stream)
                .map(mentionEntityStream ->  mentionEntityStream.map(MentionEntity::getScreenName))
                .map(stringStream -> stringStream.collect(Collectors.toSet()))
                .map(strings -> String.join(",", strings))
                .orElse("");
    }

    private static String processURLs(Tweet tweet) {
        return Optional.ofNullable(tweet.getEntities())
                .map(Entities::getUrls)
                .map(List::stream)
                .map(urlEntityStream -> urlEntityStream.map(UrlEntity::getDisplayUrl))
                .map(stringStream -> stringStream.collect(Collectors.toSet()))
                .map(strings -> String.join(",", strings))
                .orElse("");
    }

    private static String processHashTags(Tweet tweet) {
        return Optional.ofNullable(tweet.getEntities())
                .map(Entities::getHashTags)
                .map(hashTags -> String.join(",", hashTags.stream().map(HashTagEntity::getText).collect(Collectors.toSet())))
                .orElse("");
    }
}
