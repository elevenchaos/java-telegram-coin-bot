package com.entity;

import lombok.Data;

/**
 * Created by Robin Wang  on 2018/4/24.
 */
@Data
public class SubCoin {
    private String sub_user_id;//订阅用户的ID
    private String sub_coin_symbol;//订阅的种类
    private String sub_rate;//订阅频率
    private String sub_percent;//
}
