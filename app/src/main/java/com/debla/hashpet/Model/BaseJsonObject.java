package com.debla.hashpet.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Dave-PC on 2017/12/1.
 */

public class BaseJsonObject<T> {
    @SerializedName("status")
    private String status;      //
    @SerializedName("result")
    private T result;      //结果
    @SerializedName("message")
    private String message;     //消息

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
