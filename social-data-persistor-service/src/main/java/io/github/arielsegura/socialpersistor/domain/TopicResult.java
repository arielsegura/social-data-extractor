package io.github.arielsegura.socialpersistor.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;

@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TopicResult implements Serializable {

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordinal = 3)
    @NonNull
    private String id;
    private String originalId;
    @NonNull
    private String text;
    @NonNull
    private Date createdAt;
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 2)
    @NonNull
    private String mainSearch;
    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED, ordinal = 1)
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

    public TopicResult(String id, String text, Date date, String mainSearch, String searchUserId) {
        this.id = id;
        this.text = text;
        this.createdAt = date;
        this.mainSearch = mainSearch;
        this.searchUserId = searchUserId;
    }
}
