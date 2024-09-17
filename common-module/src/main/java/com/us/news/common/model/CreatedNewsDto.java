package com.us.news.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreatedNewsDto {
    private UUID newsId;
    private String title;
    private String content;
    private UUID locationId;
}
