package io.github.arielsegura.socialpersistor.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(indexName = "topicresult-index")
//TODO fixme try to refactor this code so that classes are not duplicated
public class SearchTopicResult implements Serializable {

    @NonNull
    @Id // elasticsearch
    private String id;
    private String originalId;
    @NonNull
    @Field
    private String text;
    @NonNull
    private Date createdAt;
    @NonNull
    private String mainSearch;
    @NonNull
    private String searchUserId;
    private String fromUser;
    private String profileImageUrl;
    private String fromUserId;
    private Integer shareCount;
    private boolean shared;
    private boolean isFavorite;
    private Integer favoriteCount;
    /**
     * Comma separated
     */
    private String hashTags;
    /**
     * Comma separated
     */
    private String mentions;
    private String mediaUrl;
    private boolean isAlreadyShared;

    public SearchTopicResult(String id, String text, Date date, String mainSearch, String searchUserId) {
        this.id = id;
        this.text = text;
        this.createdAt = date;
        this.mainSearch = mainSearch;
        this.searchUserId = searchUserId;
    }
}
