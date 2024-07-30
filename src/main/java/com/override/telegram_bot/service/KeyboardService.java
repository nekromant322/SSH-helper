package com.override.telegram_bot.service;

import com.override.telegram_bot.service.ServerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardService {

    @Autowired
    private ServerServiceImpl serverServiceImpl;

    public InlineKeyboardMarkup getServersInlineKeyboard() {
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        serverServiceImpl.findAllServers().forEach(server -> {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(server.getName() + " " + server.getIp());
            inlineKeyboardButton.setCallbackData(server.getIp());
            rowInline.add(inlineKeyboardButton);
            rowsInline.add(rowInline);
        });
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }
}
