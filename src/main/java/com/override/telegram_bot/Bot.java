package com.override.telegram_bot;


import com.override.telegram_bot.commands.ServiceCommand;
import com.override.telegram_bot.model.TelegramUser;
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

import java.util.List;

@Component
public class Bot extends TelegramLongPollingCommandBot {

    @Autowired
    private BotProperties botProperties;

    @Autowired
    private KeyboardService keyboardService;

    @Autowired
    private TelegramUserServiceImpl telegramUserServiceImpl;

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

    @Override
    public void processNonCommandUpdate(Update update) {
        if (telegramUserServiceImpl.isOwner(update)) {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Long chatId = update.getMessage().getChatId();
                String msgText = update.getMessage().getText();
                TelegramUser telegramUser = telegramUserServiceImpl.getTelegramUser(chatId);
                if (telegramUser != null) {
                    sendMessage(chatId, sshCommandService.execCommand(telegramUser.getServerIp(), msgText));
                } else {
                    sendMessage(chatId, sshCommandService.execCommand(msgText));
                }
            } else if (update.hasMessage() && update.getMessage().hasDocument()) {
                Long chatId = update.getMessage().getChatId();
                try {
                    Document document = update.getMessage().getDocument();
                    String caption = telegramUserServiceImpl.getNewServerUserName(update.getMessage().getCaption());
                    telegramUserServiceImpl.saveOrUpdateTelegramUser(TelegramUser.builder()
                            .chatId(chatId)
                            .docFileName(document.getFileName())
                            .docFileId(document.getFileId())
                            .caption(caption)
                            .build());
                    fileService.isValidFile(document.getFileName());
                    sendMessage(chatId, "Выбери сервер:", keyboardService.getServersInlineKeyboard());
                } catch (IllegalArgumentException e) {
                    sendMessage(chatId, e.getMessage());
                }
            } else if (update.hasCallbackQuery()) {
                String serverIp = update.getCallbackQuery().getData();
                Long chatId = update.getCallbackQuery().getMessage().getChatId();
                TelegramUser telegramUser = telegramUserServiceImpl.getTelegramUser(chatId);
                if (telegramUser == null || telegramUser.getDocFileName() == null) {
                    telegramUserServiceImpl.saveOrUpdateTelegramUser(TelegramUser.builder()
                            .chatId(chatId)
                            .serverIp(serverIp)
                            .build());
                    sendMessage(chatId, String.format(MessageContants.SERVER, serverIp));
                } else {
                    String caption = telegramUser.getCaption();
                    telegramUserServiceImpl.deleteDoc(chatId);
                    try {
                        String msg = fileService.executeLoadKeyFile(serverIp, telegramUser.getDocFileName(), telegramUser.getDocFileId(), caption, getBotToken());
                        sendMessage(chatId, msg);
                        if (msg.equals(String.format(MessageContants.FILE_LOAD_AND_USER_CREAT, telegramUser.getDocFileName(), serverIp, caption))) {
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
