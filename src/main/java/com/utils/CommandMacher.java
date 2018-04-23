package com.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Pattern;

/**
 * Created by Robin Wang  on 2018/4/23.
 */
@UtilityClass
public class CommandMacher {
    /**
     * 是否全为数字
     * @param str
     * @return
     */
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
