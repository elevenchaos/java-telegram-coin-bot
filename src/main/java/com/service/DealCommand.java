package com.service;

import com.cache.Symbol2ID;
import com.commons.Const;
import com.entity.CoinEntity;
import com.entity.Response;
import com.utils.CoinGetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by Robin Wang  on 2018-4-20.
 */
@Service("dealCommand")
public class DealCommand {
    @Autowired
    GetCoinPriceService coinService;
    @Autowired
    Symbol2ID symbol2ID;

    public Response commandHandle(String command) {
        command = command.replace("/coin", "").trim().toLowerCase();
        Response response = new Response();
        System.out.println("the command is:" + command);
        if (symbol2ID.getIdBySymbol(command) != null) {
            symbolHandle(command);
        } else if ("list".equals(command)) {
            response = coinService.getAllCoinInfos();
        } else if (command.contains("top")) {
            command = command.replace("top", "");
            response = topHandle(command);
        } else {
            response.setCode(Const.NORMAL_FAIL);
            response.setMsg("undefined command,check your command plz.");
        }
        return response;
    }

    private Response symbolHandle(String command) {
        Response response = coinService.getCoinInfoBySymbol(command);
        if (response.isSuccess()) {
            CoinEntity entity = (CoinEntity) response.getData();
            StringBuilder builde = new StringBuilder();
            builde.append(command.toUpperCase() + " 目前价格约合:\n" + entity.getPrice_btc() + " BTC\n")
                    .append(entity.getPrice_usd() + " USD\n");

            BigDecimal bigDecimalRate = CoinGetUtil.getRate("USD", "CNY");
            if (bigDecimalRate != null){
                BigDecimal bigDecimalUsd = new BigDecimal(Double.parseDouble(entity.getPrice_usd()));
                BigDecimal cnyPrice = bigDecimalUsd.multiply(bigDecimalRate);
                DecimalFormat format = new DecimalFormat("#.0000");
                builde.append(format.format(cnyPrice) + " CNY\n");
            }
            builde.append("近1小时涨跌百分比:" + entity.getPercent_change_1h() + "%\n")
                    .append("近24小时涨跌百分比:" + entity.getPercent_change_24h() + "%\n")
                    .append("近7日涨跌百分比:" + entity.getPercent_change_7d() + "%\n");
            response.setData(builde.toString());
        }
        return response;
    }

    private Response topHandle(String command) {
//        Response topRes = coinService.getTopCoinsByRank(command);
//        if (topRes.isSuccess()) {
//
//        }
        return null;
    }

}
