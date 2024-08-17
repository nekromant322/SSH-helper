package com.override.telegram_bot.commands;

import com.override.telegram_bot.enums.MessageContants;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.override.telegram_bot.enums.Commands.HELP;

@Component
public class HelpCommand extends ServiceCommand {

    public HelpCommand() {
        super(HELP.getAlias(), HELP.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        sendAnswerDefaultParseMode(absSender, chat.getId(), this.getCommandIdentifier(), user, MessageContants.HELP);
    }
}
