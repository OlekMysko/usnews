package com.us.news.common.config;

public interface BaseLocationsUrls {
   String LOCATIONS_CONTROLLER_PREFIX = "/locations";
   String LOCATION_NAME = "/{name}";
   String ID_PATH_VARIABLE = "/{id}";
   String ID = "/id";
   String LOCATION_ID =  ID+ ID_PATH_VARIABLE;
}
