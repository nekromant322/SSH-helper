package com.override.telegram_bot;


import com.override.telegram_bot.commands.ServiceCommand;
import com.override.telegram_bot.dto.TelegramUserProperties;
import com.override.telegram_bot.enums.MessageContants;
import com.override.telegram_bot.properties.BotProperties;
import com.override.telegram_bot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
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
    private UserServiceImpl userDetailsService;

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

    HashMap<Long, TelegramUserProperties> telegramFile = new HashMap<>();
    HashMap<Long, String> userServer = TelegramUserProperties.getUserServer();

    @Override
    public void processNonCommandUpdate(Update update) {
        if (telegramUserService.isOwner(update)) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Long chatId = update.getMessage().getChatId();
                String msgText = update.getMessage().getText();
                if (userServer.containsKey(chatId)) {
                    String serverIp = userServer.get(chatId);
                    sendMessage(chatId, sshCommandService.execCommand(serverIp, msgText));
                } else {
                    sendMessage(chatId, sshCommandService.execCommand(msgText));
                }
            } else if (update.hasMessage() && update.getMessage().hasDocument()) {
                Long chatId = update.getMessage().getChatId();
                try {
                    Document document = update.getMessage().getDocument();
                    String caption = telegramUserService.getNewServerUserName(update.getMessage().getCaption());
                    telegramFile.put(chatId, new TelegramUserProperties(document, caption));
                    fileService.isValidFile(document.getFileName());
                    sendMessage(chatId, "Выбери сервер:", keyboardService.getServersInlineKeyboard());
                } catch (IllegalArgumentException e) {
                    sendMessage(chatId, e.getMessage());
                }
            } else if (update.hasCallbackQuery()) {
                String serverIp = update.getCallbackQuery().getData();
                Long chatId = update.getCallbackQuery().getMessage().getChatId();
                TelegramUserProperties telegramUserProperties = telegramFile.get(chatId);
                if (telegramUserProperties == null) {
                    userServer.put(chatId, serverIp);
                    sendMessage(chatId, "Сервер выбран!");
                } else {
                    Document document = telegramUserProperties.getDocument();
                    String caption = telegramUserProperties.getCaption();
                    telegramFile.clear();
                    try {
                        String msg = fileService.executeLoadKeyFile(serverIp, document, caption, getBotToken());
                        sendMessage(chatId, msg);
                        if (msg.equals(String.format(MessageContants.FILE_LOAD_AND_USER_CREAT, document.getFileName(), serverIp, caption))) {
                            userDetailsService.createOrUpdateUserServer(serverIp, caption);
                        }
                    } catch (IllegalArgumentException e) {
                        sendMessage(chatId, e.getMessage());
                    }
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
