package com;

import com.bot.CoinBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@SpringBootApplication
@RestController
public class CoinApplication {
	public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CoinApplication.class, args);
        ApiContextInitializer.init();
        TelegramBotsApi api = new TelegramBotsApi();
        try {
            api.registerBot(new CoinBot());
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
	}
}
