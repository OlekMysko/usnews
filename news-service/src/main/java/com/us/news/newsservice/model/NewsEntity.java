package com.us.news.newsservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news")
public class NewsEntity {
    @Id
    @GeneratedValue
    @Column(name = "news_id", nullable = false)
    private UUID newsId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "location_id", nullable = false)
    private UUID locationId;
}
