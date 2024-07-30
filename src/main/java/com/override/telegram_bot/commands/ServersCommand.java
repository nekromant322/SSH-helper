package com.override.telegram_bot.commands;

import com.override.telegram_bot.enums.MessageContants;
import com.override.telegram_bot.service.ServerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.stream.Collectors;

import static com.override.telegram_bot.enums.Commands.SERVERS;

@Component
public class ServersCommand extends ServiceCommand {

    @Autowired
    private ServerServiceImpl serverService;

    public ServersCommand() {
        super(SERVERS.getAlias(), SERVERS.getDescription());
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user,
                MessageContants.LIST_SERVERS +
                        serverService.findAllServers().stream()
                                .map(server -> "👉" + " " + server.getName() +
                                        " " + server.getIp())
                                .collect(Collectors.joining("\n")));
    }
}
