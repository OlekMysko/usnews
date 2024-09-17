package com.us.news.aggregationservice.model;

import com.us.news.common.model.CreatedLocationDto;
import com.us.news.common.model.CreatedNewsDto;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AggregationResultDto {
    private CreatedLocationDto location;
    private List<CreatedNewsDto> newsList;
}