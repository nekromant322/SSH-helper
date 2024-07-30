package com.override.telegram_bot;


import com.override.telegram_bot.service.SshCommandService;
import com.override.telegram_bot.commands.ServiceCommand;
import com.override.telegram_bot.properties.BotProperties;
import com.override.telegram_bot.service.FileService;
import com.override.telegram_bot.service.KeyboardService;
import com.override.telegram_bot.service.TelegramUserService;
import com.override.telegram_bot.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class Bot extends TelegramLongPollingCommandBot {

    @Autowired
    private BotProperties botProperties;

    @Autowired
    private KeyboardService keyboardService;

    @Autowired
    private TelegramUserService telegramUserService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private FileService fileService;

    @Autowired
    private SshCommandService sshCommandService;

    public Bot(List<ServiceCommand> allCommands) {
        super();
        allCommands.forEach(this::register);
    }

    @Override
    public String getBotUsername() {
        return botProperties.getName();
    }

    @Override
    public String getBotToken() {
        return botProperties.getToken();
    }

    private Document document;
    private String caption;

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            User user = update.getMessage().getFrom();
            if (telegramUserService.isOwner(user)) {
                String msgText = update.getMessage().getText();
                sendMessage(chatId, sshCommandService.execCommand(msgText));
            }
        } else if (update.hasMessage() && update.getMessage().hasDocument()) {
            User user = update.getMessage().getFrom();
            if (telegramUserService.isOwner(user)) {
                document = update.getMessage().getDocument();
                Long chatId = update.getMessage().getChatId();
                try {
                    caption = telegramUserService.getNewServerUserName(update.getMessage().getCaption());
                    fileService.isValidFile(document.getFileName());
                    sendMessage(chatId, "Выбери сервер:", keyboardService.getServersInlineKeyboard());
                } catch (IllegalArgumentException e) {
                    sendMessage(chatId, e.getMessage());
                }
            }
        } else if (update.hasCallbackQuery()) {
            User user = update.getCallbackQuery().getFrom();
            if (telegramUserService.isOwner(user)) {
                String serverIp = update.getCallbackQuery().getData();
                Long chatId = update.getCallbackQuery().getMessage().getChatId();
                try {
                    sendMessage(chatId, fileService.executeLoadKeyFile(serverIp, document, caption, getBotToken()));
                    sendMessage(chatId, userDetailsService.createUserServer(serverIp, caption));
                } catch (IllegalArgumentException e) {
                    sendMessage(chatId, e.getMessage());
                }
            }
        }
    }

    private void sendMessage(long chatId, String msg) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(msg);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(long chatId, String msg, InlineKeyboardMarkup markupInline) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(msg);
        message.setReplyMarkup(markupInline);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
