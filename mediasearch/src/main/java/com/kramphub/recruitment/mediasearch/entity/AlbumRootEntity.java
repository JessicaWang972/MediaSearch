package com.kramphub.recruitment.mediasearch.entity;

import javax.persistence.Entity;
import java.util.List;

/**
 * Album Root Entity
 */
@Entity
public class AlbumRootEntity {
    private int resultCount;
    private List<AlbumInfoEntity> results;

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public List<AlbumInfoEntity> getResults() {
        return results;
    }

    public void setResults(List<AlbumInfoEntity> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "AlbumRootEntity{" +
                "resultCount=" + resultCount +
                ", results=" + results +
                '}';
    }
}
