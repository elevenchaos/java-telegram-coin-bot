package com.entity;

import lombok.Data;

/**
 * Created by Robin Wang  on 2018/4/20.
 */
@Data
public class CoinEntity{
    private String id;
    private String name;
    private String symbol;
    private String rank;
    private String price_usd;
    private String price_btc;
    private String market_cap_usd;
    private String available_supply;
    private String total_supply;
    private String max_supply;
    private String percent_change_1h;
    private String percent_change_24h;
    private String percent_change_7d;
    private String last_updated;

}
