package io.github.arielsegura.socialextractor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestTopicResult {
    private String id;
    private String originalId;
    private String text;
    private String searchUserId;
    private Date createdAt;
    private String mainSearch;
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

    public RestTopicResult(String id, String text, Date createdAt, String searchTerm) {
        this.id = id;
        this.text = text;
        this.createdAt = createdAt;
        this.mainSearch = searchTerm;
    }

    public void setIsAlreadyShared(boolean isAlreadyShared) {
        this.isAlreadyShared = isAlreadyShared;
    }

    public void setAlreadyShared(boolean isAlreadyShared) {
        this.isAlreadyShared = isAlreadyShared;
    }
}
