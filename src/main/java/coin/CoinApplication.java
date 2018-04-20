package coin;

import coin.bot.CoinBot;
import coin.service.DealCommand;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@SpringBootApplication
@RestController
public class CoinApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(CoinApplication.class, args);
        ApiContextInitializer.init();
        TelegramBotsApi api = new TelegramBotsApi();
        DealCommand dealCommand = (DealCommand) CoinContext.getBean("dealCommand");
        System.out.println(dealCommand.test());
        try {
            api.registerBot(new CoinBot());
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
	}
}
