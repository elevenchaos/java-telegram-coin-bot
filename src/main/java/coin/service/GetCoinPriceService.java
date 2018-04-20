package coin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import coin.cache.Symbol2ID;
import coin.commons.Const;
import coin.config.CoinConfig;
import coin.entity.CoinEntity;
import coin.entity.Response;
import coin.utils.CoinGetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取币值
 * Created by Robin Wang  on 2018/4/20.
 */
@Service
@Slf4j
public class GetCoinPriceService {
    @Autowired
    CoinConfig coinConfig;

    /**
     * 通过币名获取币的相关信息
     *
     * @param symbol
     * @return
     */
    public Response getCoinInfoBySymbol(String symbol) {
        Response response = new Response();
        String url = coinConfig.getCoin_api_url() + Symbol2ID.getIdBySymbol(symbol);
        String res = CoinGetUtil.get(url);
        if (CoinGetUtil.isAPISuccess(res)){
            JSONArray coinArray = JSONArray.parseArray(res);
            try {
                CoinEntity coinEntity = JSONObject.parseObject(coinArray.get(0).toString(),CoinEntity.class);
                response.setCode(Const.SUCCEES);
                response.setData(coinEntity);
            }catch (Exception e){
                log.error("在处理API返回数据时出现错误..."+e.getStackTrace());
                response.setCode(Const.DEAL_EXCEPTION);//处理异常 code 设置为 -1
                response.setMsg("出现异常");
            }
        }else if (res.startsWith("{")){
            //返回失败结果时的结构
            response.setCode(Const.NORMAL_FAIL);
            response.setMsg(JSONObject.parseObject(res).getString("error"));
        }else {
            response.setCode(Const.UNRESPONSE_EXCEPTION);//数据获取异常设置 code 为 -5
            response.setMsg("API 接口异常，未获取到有效的结果数据。");
        }
        return response;
    }

    /**
     * 获取所有虚拟币的数据
     * @return
     */
    public Response getAllCoinInfos(){
        String url = coinConfig.getCoin_api_url();
        String res = CoinGetUtil.get(url);
        List<CoinEntity> list = new ArrayList<>();
        Response response = new Response();
        if (CoinGetUtil.isAPISuccess(res)){
            JSONArray coinArray = JSONArray.parseArray(res);
            for (int i = 0; i < coinArray.size(); i++) {
                CoinEntity entity = JSONObject.parseObject(coinArray.getJSONArray(i).toString(),CoinEntity.class);
                list.add(entity);
            }
        }
        if (list.size() > 0)response.setCode("200");
        response.setData(list);
        return response;
    }

    public Response getTopCoinsByRank(int top_num){
        String url = coinConfig.getCoin_api_url();
        String res = CoinGetUtil.get(url);
        List<CoinEntity> list = new ArrayList<>();
        Response response = new Response();
        if (CoinGetUtil.isAPISuccess(res)){
            JSONArray coinArray = JSONArray.parseArray(res);
            int index =  top_num >coinArray.size()?coinArray.size():top_num;
            for (int i = 0; i < index ; i++) {
                CoinEntity entity = JSONObject.parseObject(coinArray.getJSONArray(i).toString(),CoinEntity.class);
                list.add(entity);
            }
        }
        if (list.size() > 0)response.setCode("200");
        response.setData(list);
        return response;
    }

}
