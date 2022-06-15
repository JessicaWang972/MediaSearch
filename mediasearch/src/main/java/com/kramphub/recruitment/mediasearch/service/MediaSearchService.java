package com.kramphub.recruitment.mediasearch.service;

import com.kramphub.recruitment.mediasearch.entity.MediaEntity;

import java.util.List;

/**
 * Search Books and Albums Service Interface
 */
public interface MediaSearchService {
    /**
     * Get Books and Albums by Title
     * @param bookTitle
     * @param albumTitle
     * @return
     */
    List<MediaEntity> getMediaByTitle(String bookTitle, String albumTitle);
}
