package com.override.telegram_bot.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "ssh-server")
public class ServerProperties {
    private String ip;
    private int port;
    private String user;
    private String pathToPrivateKey;
}
