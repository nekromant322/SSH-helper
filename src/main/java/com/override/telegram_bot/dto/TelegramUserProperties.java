package com.override.telegram_bot.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.util.HashMap;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Component
public class TelegramUserProperties {
    private Document document;
    private String caption;
    @Getter
    private static final HashMap<Long, String> userServer = new HashMap<>();
}
