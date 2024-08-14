package com.override.telegram_bot.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "owners")
@Component
public class OwnerProperties {
    private List<String> namesOwnerTelegramBot;
    private String pass;
}
