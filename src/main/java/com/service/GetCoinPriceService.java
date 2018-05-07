package com.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cache.Symbol2ID;
import com.commons.Const;
import com.config.CoinConfig;
import com.entity.CoinEntity;
import com.entity.Response;
import com.utils.CoinGetUtil;
import ctd.util.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 获取币值
 * Created by Robin Wang  on 2018/4/20.
 */
@Service
@Slf4j
public class GetCoinPriceService {
    private static Integer DEFAULT_TOP_NUM = 10;
    @Autowired
    CoinConfig coinConfig;
    @Autowired
    Symbol2ID symbol2ID;

    /**
     * 通过币名获取币的相关信息
     *
     * @param symbol
     * @return
     */
    public Response getCoinInfoBySymbol(String symbol) {
        Response response = new Response();
        String url = coinConfig.getCoin_api_url() + symbol2ID.getIdBySymbol(symbol);
        String res = CoinGetUtil.get(url);
        System.out.println("the request url is:" + url);
        System.out.println("the response is:" + res);
        if (CoinGetUtil.isAPISuccess(res)) {
            JSONArray coinArray = JSONArray.parseArray(res);
            try {
                CoinEntity coinEntity = JSONObject.parseObject(coinArray.getJSONObject(0).toString(), CoinEntity.class);
                response.setCode(Const.SUCCEES);
                response.setData(coinEntity);
            } catch (Exception e) {
                log.error("在处理API返回数据时出现错误..." + e.getStackTrace());
                response.setCode(Const.DEAL_EXCEPTION);//处理异常 code 设置为 -1
                response.setMsg("出现异常");
            }
        } else if (res.startsWith("{")) {
            //返回失败结果时的结构
            response.setCode(Const.NORMAL_FAIL);
            response.setMsg(JSONObject.parseObject(res).getString("error"));
        } else {
            response.setCode(Const.UNRESPONSE_EXCEPTION);//数据获取异常设置 code 为 -5
            response.setMsg("API 接口异常，未获取到有效的结果数据。");
        }
        return response;
    }


    /**
     * 获取所有虚拟币的数据
     *
     * @return
     */
    public Response getAllCoinInfos() {
        String url = coinConfig.getCoin_api_url();
        String res = CoinGetUtil.get(url);
        List<CoinEntity> list = null;
        Response response = new Response();
        if (CoinGetUtil.isAPISuccess(res)) {
            list = getCoinEntity(res);
        }
        if (list.size() > 0) response.setCode("200");
        response.setData(list);
        return response;
    }

    public Response getTopCoinsByPrice(Integer top_num) {
        String url = coinConfig.getCoin_api_url();
        if (top_num == null) top_num = DEFAULT_TOP_NUM;
        String res = CoinGetUtil.get(url);
        List<CoinEntity> list = null;
        Response response = new Response();
        if (CoinGetUtil.isAPISuccess(res)) {
            list = getCoinEntity(res);
            Collections.sort(list, new Comparator<CoinEntity>() {
                @Override
                public int compare(CoinEntity o1, CoinEntity o2) {
                    return Double.parseDouble(o1.getPrice_usd()) > Double.parseDouble(o2.getPrice_usd()) ? -1 : 1;
                }
            });
            StringBuilder builder = new StringBuilder();
            builder.append("价格在前" + top_num + "位的币如下:\n");
            Integer index = list.size() > top_num ? top_num : list.size();
            for (int i = 0; i < index; i++) {
                CoinEntity item = list.get(i);
                builder.append(item.getSymbol() + "-->" + item.getPrice_usd() + "\n");
            }
            response.setCode(Const.SUCCEES);
            response.setData(builder.toString());
        } else {
            response.setMsg("未获取到有效数据");
            response.setCode(Const.NORMAL_FAIL);
        }
        return response;
    }

    private List<CoinEntity>getCoinEntity(String res){
        List<CoinEntity> list = new ArrayList();
        JSONArray coinArray = JSONArray.parseArray(res);
        for (int i = 0; i < coinArray.size(); i++) {
            CoinEntity entity = JSONObject.parseObject(coinArray.getJSONObject(i).toString(), CoinEntity.class);
            if (entity.getPercent_change_1h() == null || entity.getPercent_change_24h() == null || entity.getPercent_change_7d() == null)continue;
            list.add(entity);
        }
        return list;
    }
    public Response getTopCoinsByWave(Integer top_num, String time_range) {
        if (top_num == null) top_num = DEFAULT_TOP_NUM;
        String url = coinConfig.getCoin_api_url();
        String res = CoinGetUtil.get(url);
        List<CoinEntity> list;
        Response response = new Response();
        if (CoinGetUtil.isAPISuccess(res)) {
            list = getCoinEntity(res);
            StringBuilder builder = new StringBuilder();
            if ("hour".equalsIgnoreCase(time_range) || "h".equalsIgnoreCase(time_range)) {
                System.out.println("list is:" + JSONUtils.toString(list));
                sortByWave(list, "h", "up");
                builder.append("近一小时涨幅前" + top_num + "名:\n");
                pickResponse(list, builder, top_num, "h");
                builder.append("--------\n");
                builder.append("近一小时跌幅前" + top_num + "名:\n");
                sortByWave(list, "h", "down");
                pickResponse(list, builder, top_num, "h");
            } else if ("day".equalsIgnoreCase(time_range) || "d".equalsIgnoreCase(time_range)) {
                sortByWave(list, "d", "up");
                builder.append("近一天涨幅前" + top_num + "名:\n");
                pickResponse(list, builder, top_num, "d");
                builder.append("--------\n");
                builder.append("近一天跌幅前" + top_num + "名:\n");
                sortByWave(list, "d", "down");
                pickResponse(list, builder, top_num, "d");
            } else if ("week".equalsIgnoreCase(time_range) || "w".equalsIgnoreCase(time_range)) {
                sortByWave(list, "w", "up");
                builder.append("近一周涨幅前" + top_num + "名:\n");
                pickResponse(list, builder, top_num, "w");
                builder.append("--------\n");
                builder.append("近一周跌幅前" + top_num + "名:\n");
                sortByWave(list, "w", "down");
                pickResponse(list, builder, top_num, "w");
            } else {
                response.setCode(Const.NORMAL_FAIL);
                response.setMsg("the time range is undefined,try h or d or w again.");
            }
            if (!Const.NORMAL_FAIL.equals(response.getCode())) {
                response.setCode(Const.SUCCEES);
                response.setData(builder.toString());
            }
        } else {
            response.setCode(Const.UNRESPONSE_EXCEPTION);
            response.setMsg("API 接口异常\n" + "调用链接:" + url + "\n" + "返回信息:" + res);
        }
        return response;

    }

    public static List<CoinEntity> sortByWave(List<CoinEntity> list, String type, String trade) {
        if (type.equals("h")) {
            Collections.sort(list, new Comparator<CoinEntity>() {
                @Override
                public int compare(CoinEntity o1, CoinEntity o2) {
                    return trade.equals("up") ? Double.parseDouble(o1.getPercent_change_1h()) > Double.parseDouble(o2.getPercent_change_1h()) ? -1 : 1 : Double.parseDouble(o1.getPercent_change_1h()) > Double.parseDouble(o2.getPercent_change_1h()) ? 1 : -1;
                }
            });

        } else if (type.equals("d")) {
            Collections.sort(list, new Comparator<CoinEntity>() {
                @Override
                public int compare(CoinEntity o1, CoinEntity o2) {
                    return trade.equals("up") ? Double.parseDouble(o1.getPercent_change_24h()) > Double.parseDouble(o2.getPercent_change_24h()) ? -1 : 1 : Double.parseDouble(o1.getPercent_change_24h()) > Double.parseDouble(o2.getPercent_change_24h()) ? 1 : -1;
                }
            });

        } else {
            Collections.sort(list, new Comparator<CoinEntity>() {
                @Override
                public int compare(CoinEntity o1, CoinEntity o2) {
                    return trade.equals("up") ? Double.parseDouble(o1.getPercent_change_7d()) > Double.parseDouble(o2.getPercent_change_7d()) ? -1 : 1 : Double.parseDouble(o1.getPercent_change_7d()) > Double.parseDouble(o2.getPercent_change_7d()) ? 1 : -1;
                }
            });
        }
        return list;
    }

    private void pickResponse(List<CoinEntity> list, StringBuilder builder, Integer top_num, String type) {
        Integer index = top_num > list.size() ? list.size() : top_num;
        for (int i = 0; i < index; i++) {
            CoinEntity item = list.get(i);
            builder.append(item.getSymbol() + " ---> " + (type.equals("h") ? item.getPercent_change_1h() : type.equals("d") ? item.getPercent_change_24h() : item.getPercent_change_7d()) + "%\n");
        }
    }

    public static void main(String[] args) {
        List<CoinEntity> list = new ArrayList<>();
        CoinEntity entity1 = new CoinEntity();
        entity1.setPercent_change_1h("0.01");
        CoinEntity entity2 = new CoinEntity();
        entity2.setPercent_change_1h("0.02");
        CoinEntity entity3 = new CoinEntity();
        entity3.setPercent_change_1h("-22.03");
        list.add(entity2);
        list.add(entity1);
        list.add(entity3);
        sortByWave(list, "h", "down");
        System.out.println(JSONUtils.toString(list));
    }
}
