package com.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 配置文件的加载
 * Created by Robin Wang  on 2018/4/20.
 */
@Component
@Data
@ConfigurationProperties
@PropertySource("classpath:coin.properties")
public class CoinConfig {
    private String coin_api_url;
}
