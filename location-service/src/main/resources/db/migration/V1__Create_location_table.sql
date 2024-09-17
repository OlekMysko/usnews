CREATE TABLE IF NOT EXISTS location (
                                        location_id BINARY(16) PRIMARY KEY,
                                        location_name VARCHAR(255) NOT NULL,
                                        location_latitude DOUBLE NOT NULL,
                                        location_longitude DOUBLE NOT NULL
);
