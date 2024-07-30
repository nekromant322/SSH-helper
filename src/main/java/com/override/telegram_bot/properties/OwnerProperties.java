package com.override.telegram_bot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "owners")
public class OwnerProperties {
    private List<String> userNamesTelegram;
}
