package com.kramphub.recruitment.mediasearch.entity;

import javax.persistence.Entity;

/**
 * Book Item Entity
 */
@Entity
public class BookItemEntity {
    private String kind;
    private String id;
    private String etag;
    private String selfLink;
    private BookVolumeInfoEntity volumeInfo;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public BookVolumeInfoEntity getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(BookVolumeInfoEntity volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    @Override
    public String toString() {
        return "BookItemEntity{" +
                "kind='" + kind + '\'' +
                ", id='" + id + '\'' +
                ", etag='" + etag + '\'' +
                ", selfLink='" + selfLink + '\'' +
                ", volumeInfo=" + volumeInfo +
                '}';
    }
}
