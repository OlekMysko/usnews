package com.us.news.aggregationservice.api_provider;


import com.us.news.aggregationservice.api_provider.model.Coordinates;

import java.util.Optional;

public interface LocationDetailsProvider {
    Optional<Coordinates> fetchCoordinates(String locationName);
}
