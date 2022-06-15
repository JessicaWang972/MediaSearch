package com.kramphub.recruitment.mediasearch.entity;

import javax.persistence.Entity;
import java.util.List;

/**
 * Book Root Entity
 */
@Entity
public class BookRootEntity {
    String kind;
    int totalItems;
    List<BookItemEntity> items;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public List<BookItemEntity> getItems() {
        return items;
    }

    public void setItems(List<BookItemEntity> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "BookRootEntity{" +
                "kind='" + kind + '\'' +
                ", totalItems=" + totalItems +
                ", items=" + items +
                '}';
    }
}
