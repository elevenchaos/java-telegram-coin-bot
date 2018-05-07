package com.utils;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Robin Wang  on 2018/4/20.
 */
@UtilityClass
public class CoinGetUtil {
    private static ConcurrentHashMap<String, BigDecimal> rateCache = null;

    /**
     * com-API 请求方法
     *
     * @param url
     * @return
     */
    public String get(String url) {
        String result = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            System.out.println("send : " + url);
            Response response = client.newCall(request).execute();
            result = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("res : " + result);
        return result;
    }


    public boolean isAPISuccess(String res) {
        return StringUtils.isNotEmpty(res) && res.startsWith("[");
    }


    public BigDecimal getRate(String source, String target) {
        boolean overdue;
        if (rateCache == null) {
            overdue = true;
        } else {
            long time = (Long.parseLong(rateCache.get("rateTime").toString()) - System.currentTimeMillis()) / 1000;
            overdue = time > 3600;
        }
        if (overdue) {
            String rateUrl = "http://api.k780.com/?app=finance.rate&scur=" + source + "&tcur=" + target + "&appkey=33040&sign=487ff10ad28d8e4737fd887ae0400619";
            String rateRes = CoinGetUtil.get(rateUrl);
            JSONObject rateJson = JSONObject.parseObject(rateRes);
            if ("1".equals(rateJson.getString("success"))) {
                rateCache = new ConcurrentHashMap<>(10);
                rateCache.put("rateTime", new BigDecimal(System.currentTimeMillis()));
                JSONObject resultJson = JSONObject.parseObject(rateJson.getString("result"));
                Double rate = resultJson.getDouble("rate");
                BigDecimal bigDecimalRate = new BigDecimal(rate);
                rateCache.put("rate", bigDecimalRate);
                return bigDecimalRate;
            }
        } else {
            return rateCache.get("rate");
        }
        return null;
    }

    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(formatter.format(localDateTime));

        String str = "2008年08月23日 23:59:59";
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
        LocalDateTime localDateTime2 = LocalDateTime.parse(str, formatter2);
        System.out.println(localDateTime2);
    }
}
