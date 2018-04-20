package com.bot;

import com.entity.Response;
import com.service.DealCommand;
import ctd.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

/**
 * Created by Robin Wang  on 2018/4/20.
 */
public class CoinBot extends TelegramLongPollingBot {

    @Autowired
    DealCommand dealCommand;
    @Override
    public void onUpdateReceived(Update update) {
        String command = update.getMessage().getText();
        System.out.println(" the message is :" + JSONUtils.toString(update));
        Response response = null;
        String messgae = null;
        if (command != null) response = dealCommand.commandHandle(command);
        if (response.isSuccess()){
            messgae = (String)response.getData();
        }
        SendMessage sender = new SendMessage().setChatId(update.getMessage().getChatId())
                .setText(messgae);
        try {
            execute(sender);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public String getBotUsername() {
        return "ruby_coin_bot";
    }

    @Override
    public String getBotToken() {
        return "554926359:AAGf3bgK_tYSCvTCRW2OKVReCQhjeg0ByRI";
    }
}
