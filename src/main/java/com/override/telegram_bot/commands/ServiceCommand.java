package com.override.telegram_bot.commands;

import com.override.telegram_bot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class ServiceCommand extends BotCommand {

    ServiceCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Autowired
    private TelegramUserService telegramUserService;

    protected void sendAnswer(AbsSender absSender, Long chatId, String commandName, User user, String text) {
        if (telegramUserService.isOwner(user)) {
            SendMessage message = new SendMessage();
            message.enableMarkdown(true);
            message.setParseMode("HTML");
            message.setChatId(chatId.toString());
            message.setText(text);

            try {
                absSender.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    protected void sendAnswerDefaultParseMode(AbsSender absSender, Long chatId, String commandName, User user, String text) {
        if (telegramUserService.isOwner(user)) {
            SendMessage message = new SendMessage();
            message.enableMarkdown(true);
            message.setChatId(chatId.toString());
            message.setText(text);

            try {
                absSender.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    protected void sendKeyboard(AbsSender absSender, Long chatId, String commandName, User user, String msg, InlineKeyboardMarkup markupInline) {
        if (telegramUserService.isOwner(user)) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText(msg);
            message.setReplyMarkup(markupInline);
            try {
                absSender.execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
