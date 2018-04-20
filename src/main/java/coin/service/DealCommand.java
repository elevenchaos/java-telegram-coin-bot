package coin.service;

import coin.cache.Symbol2ID;
import coin.entity.CoinEntity;
import coin.commons.Const;
import coin.entity.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Robin Wang  on 2018-4-20.
 */
@Service("dealCommand")
public class DealCommand {
    @Autowired
    GetCoinPriceService coinService;

    public static void main(String[] args) {
        String test = "/coin eos";
        System.out.println(test.replace("/coin","").trim().toLowerCase());
    }
    public Response commandHandle(String command){
        command = command.replace("/coin","").trim().toLowerCase();
        Response response = new Response();
        if (Symbol2ID.getIdBySymbol(command) !=  null){
           response =  coinService.getCoinInfoBySymbol(command);
            if (response.isSuccess()){
                CoinEntity entity = (CoinEntity) response.getData();
                StringBuilder builde = new StringBuilder();
                builde.append(command.toUpperCase()+" 目前价格约合:\n"+entity.getPrice_btc()+" BTC\n")
                           .append(entity.getPrice_usd()+" USD\n")
                           .append("近1小时涨跌百分比:"+entity.getPercent_change_1h()+"%")
                        .append("近24小时涨跌百分比:"+entity.getPercent_change_24h()+"%")
                        .append("近7日涨跌百分比:"+entity.getPercent_change_1h()+"%");
                response.setData(builde.toString());
            }
        }else if("list".equals(command)){
            response =  coinService.getAllCoinInfos();
        }else if (command.contains("top")){
            command = command.replace("top","");
            try {
                response = coinService.getTopCoinsByRank(Integer.parseInt(command));
            }catch (Exception e){
                response.setMsg("throw some exception when deal your command");
                response.setCode(Const.DEAL_EXCEPTION);
            }
        }else {
            response.setCode(Const.NORMAL_FAIL);
            response.setMsg("undefined command,check your command plz.");
        }
        return response;
    }

    public String test(){
        return "test dahd";
    }
}
