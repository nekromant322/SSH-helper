package com.override.telegram_bot.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Document;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class TelegramFileDTO {
    private Document document;
    private String caption;
}
