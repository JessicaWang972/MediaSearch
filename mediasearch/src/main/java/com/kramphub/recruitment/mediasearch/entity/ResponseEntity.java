package com.kramphub.recruitment.mediasearch.entity;

import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Response Entity
 * @param <T>
 */
@Entity
public class ResponseEntity<T> implements Serializable {
    private int code;
    private T data;
    private String msg;

    public ResponseEntity() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
