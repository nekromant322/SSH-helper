package com.override.telegram_bot.commands;

import com.override.telegram_bot.enums.MessageContants;
import com.override.telegram_bot.properties.ServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.override.telegram_bot.enums.Commands.WEB;

@Component
public class WebCommand extends ServiceCommand {

    @Autowired
    private ServerProperties serverProperties;

    @Value("${server.port}")
    private String port;

    public WebCommand() {
        super(WEB.getAlias(), WEB.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String msg = String.format(MessageContants.WEB_URL, serverProperties.getIp(), port);
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user, msg);
    }
}
