package com.override.telegram_bot.commands;

import com.override.telegram_bot.enums.MessageContants;
import com.override.telegram_bot.service.KeyboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.override.telegram_bot.enums.Commands.SERVERS;

@Component
public class ServersCommand extends ServiceCommand {

    @Autowired
    private KeyboardService keyboardService;

    public ServersCommand() {
        super(SERVERS.getAlias(), SERVERS.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendKeyboard(absSender, chat.getId(), this.getCommandIdentifier(), user,
                MessageContants.SERVER_FOR_EXEC_COMMAND, keyboardService.getServersInlineKeyboard());
    }
}
