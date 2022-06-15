package com.kramphub.recruitment.eurekaconsumer.controller;

import com.kramphub.recruitment.eurekaconsumer.entity.MediaEntity;
import com.kramphub.recruitment.eurekaconsumer.entity.ResponseEntity;
import com.kramphub.recruitment.eurekaconsumer.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @RequestMapping("/mediaResult")
    @ResponseBody
    public ResponseEntity<List<MediaEntity>> showOptionsData(@RequestParam(value = "bookTitle",required = true) String bookTitle,
                                                             @RequestParam(value = "albumTitle",required = true) String albumTitle){
        return searchService.getMediaByTitle(bookTitle, albumTitle);
    }
}
