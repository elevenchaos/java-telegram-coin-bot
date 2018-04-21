package com.service;

import com.cache.Symbol2ID;
import com.commons.Const;
import com.entity.CoinEntity;
import com.entity.Response;
import com.utils.CoinGetUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

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
        Response response = new Response();
        if(isNumeric(command)){
            StringBuilder builder = new StringBuilder();
            builder.append("近期涨跌总览:\n");
            Response hRes = coinService.getTopCoinsByWave(StringUtils.isEmpty(command)?null:Integer.parseInt(command),"h");
            Response dRes =  coinService.getTopCoinsByWave(StringUtils.isEmpty(command)?null:Integer.parseInt(command),"d");
            Response wRes = coinService.getTopCoinsByWave(StringUtils.isEmpty(command)?null:Integer.parseInt(command),"w");
            if (hRes.isSuccess()) {
                builder.append(hRes.getData());
                builder.append("-------");
            }
            if (dRes.isSuccess()){
                builder.append(dRes.getData());
                builder.append("-------");
            }
            if (wRes.isSuccess()){
                builder.append(wRes.getData());
                builder.append("-------");
            }
            response.setCode("200");
            response.setData(builder.toString());
        }else if (command.contains("h")){
            command = command.replace("h","");
            response = coinService.getTopCoinsByWave(StringUtils.isEmpty(command)?null:Integer.parseInt(command),"h");
        }else if (command.contains("d")){
            command = command.replace("d","");
            response = coinService.getTopCoinsByWave(StringUtils.isEmpty(command)?null:Integer.parseInt(command),"d");
        }else if (command.contains("w")){
            command = command.replace("w","");
            response = coinService.getTopCoinsByWave(StringUtils.isEmpty(command)?null:Integer.parseInt(command),"w");
        }else if (command.contains("p")){
            command = command.replace("p","");
            response = coinService.getTopCoinsByPrice(StringUtils.isEmpty(command)?null:Integer.parseInt(command));
        }
        return response;
    }
    private boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
