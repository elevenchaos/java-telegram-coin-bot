package com.entity;

import lombok.Data;

/**
 * Created by Robin Wang  on 2018-4-20.
 */
@Data
public class Response {
    private String code;//200:成功
    private String msg;
    private Object data;
    public boolean isSuccess(){
        return "200".equals(code);
    }
}
