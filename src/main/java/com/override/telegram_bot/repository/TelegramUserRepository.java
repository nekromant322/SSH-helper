package com.override.telegram_bot.repository;

import com.override.telegram_bot.model.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface TelegramUserRepository extends CrudRepository<TelegramUser, Long> {
    TelegramUser findTelegramUserByChatId(Long chatId);

}
