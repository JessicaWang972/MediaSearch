package com.kramphub.recruitment.mediasearch.entity;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Media Entity
 */
@Entity
public class MediaEntity implements Serializable {
    private String title;
    private String author;
    private String publishedDate;
    private String type;

    public MediaEntity() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MediaEntity{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
