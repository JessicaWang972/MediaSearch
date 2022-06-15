package com.kramphub.recruitment.eurekaconsumer.service;

import com.kramphub.recruitment.eurekaconsumer.entity.MediaEntity;
import com.kramphub.recruitment.eurekaconsumer.entity.ResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mediaSearch", path = "/media")
@Component
public interface SearchService {

    @RequestMapping(value = "/getMediaByTitle", method = RequestMethod.GET)
    public ResponseEntity<List<MediaEntity>> getMediaByTitle(@RequestParam(value = "bookTitle",required = true) String bookTitle,
                                                             @RequestParam(value = "albumTitle",required = true) String albumTitle);
}
