package com.config;

        import lombok.Data;
        import org.springframework.boot.context.properties.ConfigurationProperties;
        import org.springframework.stereotype.Component;

/**
 * Created by Robin Wang  on 2018/4/20.
 */
@Component
@ConfigurationProperties("coin.properties")
@Data
public class CoinConfig {
    private String coin_api_url = "https://api.coinmarketcap.com/v1/";
}
