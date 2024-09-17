package com.us.news.newsservice.controller;

import com.us.news.common.model.CreatedNewsDto;
import com.us.news.newsservice.model.CreateNewsDto;

import com.us.news.common.model.ResponseWrapper;
import com.us.news.newsservice.service.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.us.news.common.config.BaseNewsUrls.*;


@RestController
@RequestMapping(NEWS_CONTROLLER_PREFIX)
@AllArgsConstructor
public class NewsController {
    private final NewsService newsService;
    private static final String NEWS_FOUND = "News found.";
    private static final String NEWS_NOT_FOUND = "News not found.";

    @PostMapping
    public ResponseEntity<ResponseWrapper<CreatedNewsDto>> addNews(@RequestBody CreateNewsDto createNewsDto) {
        ResponseWrapper<CreatedNewsDto> response = newsService.addNewNews(createNewsDto);
        HttpStatus status = response.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<CreatedNewsDto>>> getAllNews() {
        ResponseWrapper<List<CreatedNewsDto>> response = newsService.findAllNews();
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(response, status);
    }

    @GetMapping(NEWS_ID)
    public ResponseEntity<ResponseWrapper<CreatedNewsDto>> getNewsById(@PathVariable UUID id) {
        Optional<CreatedNewsDto> news = newsService.findByNewsId(id);
        return news.map(createdNewsDto -> new ResponseEntity<>(
                        new ResponseWrapper<>(true, NEWS_FOUND, createdNewsDto), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(new ResponseWrapper<>(false, NEWS_NOT_FOUND, null), HttpStatus.NOT_FOUND));
    }

    @GetMapping(NEWS_LOCATION)
    public ResponseEntity<ResponseWrapper<List<CreatedNewsDto>>> getNewsByLocationId(@PathVariable UUID locationId) {
        ResponseWrapper<List<CreatedNewsDto>> response = newsService.findNewsByLocationId(locationId);
        HttpStatus status = response.isSuccess() ? HttpStatus.OK : HttpStatus.NO_CONTENT;
        return new ResponseEntity<>(response, status);
    }
}
