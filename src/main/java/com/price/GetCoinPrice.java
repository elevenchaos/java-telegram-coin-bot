package com.price;

import com.config.CoinConfig;
import com.entity.CoinEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 获取币值
 * Created by Robin Wang  on 2018/4/20.
 */
@Service
public class GetCoinPrice {
    @Autowired
    CoinConfig coinConfig;

    /**
     * 通过币名获取币的相关信息
     *
     * @param symbol
     * @return
     */
    public CoinEntity getCoinInfoBySymbol(String symbol) {
        String url = coinConfig.getCoin_api_url() + symbol;

    }

}
