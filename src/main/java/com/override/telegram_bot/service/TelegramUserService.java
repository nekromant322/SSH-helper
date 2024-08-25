package com.override.telegram_bot.service;

import com.override.telegram_bot.model.TelegramUser;
import com.override.telegram_bot.enums.MessageContants;
import com.override.telegram_bot.properties.OwnerProperties;
import com.override.telegram_bot.repository.TelegramUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class TelegramUserService {

    @Autowired
    private OwnerProperties ownerProperties;

    @Autowired
    private TelegramUserRepository telegramUserRepository;

    public boolean isOwner(User user) {
        return ownerProperties.getNamesOwnerTelegramBot().contains(user.getUserName());
    }

    public boolean isOwner(Update update) {
        return isOwner(Optional.ofNullable(update)
                .map(Update::getMessage).map(Message::getFrom)
                .orElseGet(() -> update.getCallbackQuery().getFrom()));
    }

    public String getNewServerUserName(String user) {
        Pattern p = Pattern.compile("(([a-zA-Z].*[0-9])|([a-zA-Z].*))");
        if (user != null && user.matches(p.pattern())) {
            return user.toLowerCase().trim();
        }
        throw new IllegalArgumentException(MessageContants.ERROR_USER_NAME);
    }

    public TelegramUser getTelegramUser(Long chatId) {
        return telegramUserRepository.findTelegramUserByChatId(chatId);
    }

    public void saveOrUpdateTelegramUser(TelegramUser user) {
        Optional<TelegramUser> optionalTelegramUser = Optional.ofNullable(telegramUserRepository.findTelegramUserByChatId(user.getChatId()));
        if (optionalTelegramUser.isPresent()) {
            TelegramUser newTelegramUser = optionalTelegramUser.get();
            newTelegramUser.setChatId(user.getChatId());
            newTelegramUser.setDocFileId(user.getDocFileId());
            newTelegramUser.setDocFileName(user.getDocFileName());
            newTelegramUser.setCaption(user.getCaption());
            newTelegramUser.setServerIp(user.getServerIp());
            telegramUserRepository.save(newTelegramUser);
            return;
        }
        telegramUserRepository.save(user);
    }

    public void deleteDoc(Long chatId) {
        TelegramUser telegramUser = telegramUserRepository.findTelegramUserByChatId(chatId);
        telegramUser.setDocFileName(null);
        telegramUser.setDocFileId(null);
        telegramUser.setCaption(null);
        telegramUserRepository.save(telegramUser);
    }
}
