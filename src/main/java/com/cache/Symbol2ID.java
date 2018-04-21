package com.cache;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.config.CoinConfig;
import com.utils.CoinGetUtil;
import ctd.util.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Robin Wang  on 2018/4/20.
 */
@Slf4j
@Component
public class Symbol2ID {
    private static ConcurrentHashMap<String, String> symbolCache = null;
    @Autowired
    CoinConfig coinConfig;

    private void initTheCache(){
        symbolCache = new ConcurrentHashMap<>(1000);
        String url = coinConfig.getCoin_api_url();
        String res = CoinGetUtil.get(url);
        if (CoinGetUtil.isAPISuccess(res)){
            JSONArray coinArray = JSONArray.parseArray(res);
            for (int i = 0; i < coinArray.size(); i++) {
                JSONObject item = coinArray.getJSONObject(i);
                symbolCache.put(item.getString("symbol").toLowerCase(), item.getString("id"));
            }
            log.info("init the symbol cache success... the cache is:"+ JSONUtils.toString(symbolCache));
        }
    }
    public String getIdBySymbol(String symbol){
        if (symbolCache == null) initTheCache();
        return symbolCache.get(symbol);
    }
}
