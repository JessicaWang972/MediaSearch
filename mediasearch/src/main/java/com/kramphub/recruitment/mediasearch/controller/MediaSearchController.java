package com.kramphub.recruitment.mediasearch.controller;

import com.kramphub.recruitment.mediasearch.entity.MediaEntity;
import com.kramphub.recruitment.mediasearch.entity.ResponseEntity;
import com.kramphub.recruitment.mediasearch.enums.HttpStatusCode;
import com.kramphub.recruitment.mediasearch.service.MediaSearchService;
import com.kramphub.recruitment.mediasearch.service.implement.MediaSearchServiceImpl;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Search Books and Albums Controller Class
 */
@RestController
@RequestMapping("/media")
public class MediaSearchController {
    @Autowired
    private MediaSearchService mediaSearchService;

    private Logger logger = Logger.getLogger(getClass());

    public static Boolean responseFlag = true;

    /**
     * Get Books and Albums by Title
     * @param bookTitle
     * @param albumTitle
     * @return
     */
    @RequestMapping(value = "getMediaByTitle", method = RequestMethod.GET)
    @ResponseBody
    @HystrixCommand(fallbackMethod = "mediaFallbackMethodTimeout",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "THREAD"),
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "60000"),
                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2")},
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "5"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            })
    public ResponseEntity<List<MediaEntity>> getMediaByTitle(@RequestParam(value = "bookTitle",required = true) String bookTitle,
                                                             @RequestParam(value = "albumTitle",required = true) String albumTitle) {
        ResponseEntity responseEntity = new ResponseEntity<List<MediaEntity>>();
        try{
            List<MediaEntity> media = mediaSearchService.getMediaByTitle(bookTitle, albumTitle);
            responseEntity.setData(media);
            if((MediaSearchServiceImpl.bookResponseFlag == false) || (MediaSearchServiceImpl.albumResponseFlag == false)){
                responseFlag = false;
            }
            responseEntity.setCode(HttpStatusCode.HTTP_OK);
            return responseEntity;
        }catch (Exception e){
            logger.info("Exception Information:{}", e);
        }
        return responseEntity;
    }

    public ResponseEntity<List<MediaEntity>>  mediaFallbackMethodTimeout(String bookTitle, String albumTitle){
        System.out.println("--------Execute Timeout Degradation Policy--------");
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setCode(101);
        responseEntity.setMsg("The server is busy, please try again later!");
        return responseEntity;
    }


}
