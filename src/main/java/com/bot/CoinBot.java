package com.bot;

import ctd.util.JSONUtils;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

/**
 * Created by Robin Wang  on 2018/4/20.
 */
public class CoinBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(JSONUtils.toString(update.getMessage()));
        SendMessage sender = new SendMessage().setChatId(update.getMessage().getChatId())
                .setText("hello coin test");
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
