package com.kramphub.recruitment.mediasearch.service.implement;

import com.kramphub.recruitment.mediasearch.entity.*;
import com.kramphub.recruitment.mediasearch.service.MediaSearchService;
import com.kramphub.recruitment.mediasearch.utils.HttpClientUtil;

import com.kramphub.recruitment.mediasearch.utils.JsonUtil;
import org.apache.commons.collections4.ListUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

/**
 * Search Books and Albums ServiceImpl Class
 */
@Service
public class MediaSearchServiceImpl implements MediaSearchService {

    private Logger logger = Logger.getLogger(getClass());

    public static Boolean bookResponseFlag = true;
    public static Boolean albumResponseFlag = true;


    @Value("${book.recordNumber}")
    private int bookRecordNumber;

    @Value("${album.recordNumber}")
    private int albumRecordNumber;


    @Override
    public List<MediaEntity> getMediaByTitle(String bookTitle, String albumTitle) {
        List<MediaEntity> finalMediaEntity = new ArrayList<>();
        ExecutorService executor = Executors.newCachedThreadPool();
        FutureTask<List<MediaEntity>> future =
                new FutureTask<List<MediaEntity>>(new Callable<List<MediaEntity>>() {
                    public List<MediaEntity> call() {
                        long bookStartTime=System.currentTimeMillis();
                        List<MediaEntity> bookMediaEntities = getBooks(bookTitle);
                        logger.info("TIME SPENT OF BOOK SERVICE  : " + (System.currentTimeMillis() - bookStartTime) + "millisecond");
                        long albumStartTime=System.currentTimeMillis();
                        List<MediaEntity> albumMediaEntities = getAlbums(albumTitle);
                        logger.info("TIME SPENT OF ALBUM SERVICE  : " + (System.currentTimeMillis() - albumStartTime) + "millisecond");
                        List<MediaEntity> finalMediaEntity = ListUtils.union(bookMediaEntities, albumMediaEntities);
                        finalMediaEntity.sort(Comparator.comparing(MediaEntity::getTitle));
                        return finalMediaEntity;
                    }
                });
        executor.execute(future);
        try {
            //Get the result and set the timeout execution time to 1 minute
            finalMediaEntity = future.get(60000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            future.cancel(true);
        } catch (ExecutionException e) {
            future.cancel(true);
        } catch (TimeoutException e) {
            future.cancel(true);
        } finally {
            executor.shutdown();
        }
        return finalMediaEntity;
    }

    /**
     * Get Books Using Google Books API
     * @param bookTitle
     * @return
     */

    public List<MediaEntity> getBooks(String bookTitle){
        String url = "https://www.googleapis.com/books/v1/volumes" + "?q=" + bookTitle;
        List<String> resultList = HttpClientUtil.doGet(url);
        String result = resultList.get(0);
        String bookResponseCode = resultList.get(1);
        // If bookResponseCode is 5XX, then the server is not available. For example, 503-Service Unavailable
        if(bookResponseCode.startsWith("5")){
            bookResponseFlag = false;
        }

        BookRootEntity bookRootEntity = JsonUtil.string2Obj(result,BookRootEntity.class);
        List<BookItemEntity> bookItemEntities = new ArrayList<>();
        if(bookRootEntity != null){
            bookItemEntities  = bookRootEntity.getItems();
        }

        List<MediaEntity> mediaEntities = new ArrayList<>();
        if(!CollectionUtils.isEmpty(bookItemEntities)){
            for(BookItemEntity bookItemEntity: bookItemEntities){
                MediaEntity mediaEntity = new MediaEntity();
                BookVolumeInfoEntity bookVolumeInfoEntity = bookItemEntity.getVolumeInfo();
                // get value from bookVolumeInfoEntity
                List<String> authorsList = bookVolumeInfoEntity.getAuthors();
                if(!CollectionUtils.isEmpty(authorsList)){
                    String authors = String.join(",", authorsList);
                    // set value into mediaEntity
                    mediaEntity.setAuthor(authors);
                }
                String title = bookVolumeInfoEntity.getTitle();
                String publishedDate = bookVolumeInfoEntity.getPublishedDate();

                mediaEntity.setTitle(title);
                mediaEntity.setPublishedDate(publishedDate);
                mediaEntity.setType("Book");
                mediaEntities.add(mediaEntity);
            }
            //Only a maximum of 5 records can be returned
            if(bookRecordNumber > 5){
                bookRecordNumber = 5;
            }

            if(!CollectionUtils.isEmpty(mediaEntities)){
                // The list is sorted by publication date in descending order
                mediaEntities.sort(Comparator.comparing(MediaEntity::getPublishedDate).reversed());
                if(mediaEntities.size() > 5) {
                    // Get the first 5 pieces of data
                    List newList = mediaEntities.subList(0, bookRecordNumber);
                    return newList;
                }
            }
        }
        return mediaEntities;
    }

    /**
     * Get Albums Using iTunes API
     * @param albumTitle
     * @return
     */
    public List<MediaEntity> getAlbums(String albumTitle){
        List<MediaEntity> mediaEntities = new ArrayList<>();
        try {
            // Convert ordinary string to application/x-www-from-urlencoded string
            String albumTitleEncode = URLEncoder.encode(albumTitle, "UTF-8");
            System.out.println(albumTitleEncode);

            String url = "https://itunes.apple.com/search" + "?term=" + albumTitleEncode + "&entity=album";
            List<String> resultList = HttpClientUtil.doGet(url);
            String result = resultList.get(0);
            String albumResponseCode = resultList.get(1);
            if(albumResponseCode.startsWith("5")){
                albumResponseFlag = false;
            }
            AlbumRootEntity albumRootEntity = JsonUtil.string2Obj(result, AlbumRootEntity.class);
            List<AlbumInfoEntity> albumInfoEntities = new ArrayList<>();
            if(albumRootEntity != null){
                albumInfoEntities  = albumRootEntity.getResults();
            }

            if(!CollectionUtils.isEmpty(albumInfoEntities)){
                for(AlbumInfoEntity albumInfoEntity: albumInfoEntities){
                    MediaEntity mediaEntity = new MediaEntity();
                    String artistName = albumInfoEntity.getArtistName();
                    String albumName = albumInfoEntity.getCollectionName();
                    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
                    String releaseDate = ft.format(albumInfoEntity.getReleaseDate());

                    mediaEntity.setTitle(albumName);
                    mediaEntity.setAuthor(artistName);
                    mediaEntity.setPublishedDate(releaseDate);
                    mediaEntity.setType("Album");
                    mediaEntities.add(mediaEntity);
                }

                //Only a maximum of 5 records can be returned
                if(albumRecordNumber > 5){
                    albumRecordNumber = 5;
                }

                if(!CollectionUtils.isEmpty(mediaEntities)){
                    // The list is sorted by publication date in descending order
                    mediaEntities.sort(Comparator.comparing(MediaEntity::getPublishedDate).reversed());
                    if(mediaEntities.size() > 5) {
                        // Get the first 5 pieces of data
                        List newList = mediaEntities.subList(0, albumRecordNumber);
                        return newList;
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            logger.info("Album Search Exception Information:{}", e);
        }

        return mediaEntities;
    }




}
