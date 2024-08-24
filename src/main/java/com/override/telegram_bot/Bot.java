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

    @Override
    public void processNonCommandUpdate(Update update) {
        if (!telegramUserService.isOwner(update)) {
            return;
        }
        if (isText(update)) {
            Long chatId = update.getMessage().getChatId();
            String msgText = update.getMessage().getText();
            TelegramUser telegramUser = telegramUserService.getTelegramUser(chatId);
            execTextCommandOnRemoteServer(telegramUser, chatId, msgText);
        } else if (isDocument(update)) {
            Long chatId = update.getMessage().getChatId();
            Document document = update.getMessage().getDocument();
            String caption = telegramUserService.getNewServerUserName(update.getMessage().getCaption());
            saveDocument(chatId, document, caption);
        } else if (isButtonPress(update)) {
            String serverIp = update.getCallbackQuery().getData();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            TelegramUser telegramUser = telegramUserService.getTelegramUser(chatId);
            if (isNoDocument(telegramUser)) {
                saveChosenServerIp(chatId, serverIp);
            } else {
                createUserOnRemoteServer(serverIp, telegramUser, chatId);
                telegramUserService.deleteDoc(chatId);
            }
        }
    }

    private void createUserOnRemoteServer(String serverIp, TelegramUser telegramUser, Long chatId) {
        String caption =  telegramUser.getCaption();
        try {
            String msg = fileService.executeUploadKeyFileAndCreateUser(serverIp, telegramUser.getDocFileName(), telegramUser.getDocFileId(), caption, getBotToken());
            sendMessage(chatId, msg);
            if (isUserWasCreatedOnServer(serverIp, telegramUser, msg, caption)) {
                userDetailsService.createOrUpdateUserServer(serverIp, caption);
            }
        } catch (IllegalArgumentException e) {
            sendMessage(chatId, e.getMessage());
        }
    }

    private static boolean isUserWasCreatedOnServer(String serverIp, TelegramUser telegramUser, String msg, String caption) {
        return msg.equals(String.format(MessageContants.FILE_LOAD_AND_USER_CREAT, telegramUser.getDocFileName(), serverIp, caption));
    }

    private void saveChosenServerIp(Long chatId, String serverIp) {
        telegramUserService.saveOrUpdateTelegramUser(TelegramUser.builder()
                .chatId(chatId)
                .serverIp(serverIp)
                .build());
        sendMessage(chatId, String.format(MessageContants.CHOSEN_SERVER, serverIp));
    }

    private static boolean isNoDocument(TelegramUser telegramUser) {
        return telegramUser == null || telegramUser.getDocFileName() == null;
    }

    private void saveDocument(Long chatId, Document document, String caption) {
        try {
            telegramUserService.saveOrUpdateTelegramUser(TelegramUser.builder()
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
    }

    private void execTextCommandOnRemoteServer(TelegramUser telegramUser, Long chatId, String msgText) {
        if (telegramUser != null) {
            sendMessage(chatId, sshCommandService.execCommand(telegramUser.getServerIp(), msgText));
        } else {
            sendMessage(chatId, sshCommandService.execCommand(msgText));
        }
    }

    private static boolean isButtonPress(Update update) {
        return update.hasCallbackQuery();
    }

    private static boolean isDocument(Update update) {
        return update.hasMessage() && update.getMessage().hasDocument();
    }

    private static boolean isText(Update update) {
        return update.hasMessage() && update.getMessage().hasText();
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
