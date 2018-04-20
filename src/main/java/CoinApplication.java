import com.bot.CoinBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@SpringBootApplication
@RestController
//@ImportResource("classpath:spring-client.xml")
public class CoinApplication {

    @RequestMapping("/")
    public String index(){
        return "Hello Spring boot :)";
    }
	public static void main(String[] args) {
		SpringApplication.run(CoinApplication.class, args);
        ApiContextInitializer.init();
        TelegramBotsApi api = new TelegramBotsApi();
        try {
            api.registerBot(new CoinBot());
        }catch (TelegramApiException e){
            e.printStackTrace();
        }
	}
}
