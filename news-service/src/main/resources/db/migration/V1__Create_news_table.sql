CREATE TABLE IF NOT EXISTS news
(
    news_id     BINARY(16) PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    content     TEXT         NOT NULL,
    location_id BINARY(16)   NOT NULL
);