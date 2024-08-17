package com.override.telegram_bot.commands;

import com.override.telegram_bot.enums.BashCommands;
import com.override.telegram_bot.service.SshCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static com.override.telegram_bot.enums.Commands.DOCKER;

@Component
public class DockerPsCommand extends ServiceCommand {

    public DockerPsCommand() {
        super(DOCKER.getAlias(), DOCKER.getDescription());
    }

    @Autowired
    private SshCommandService sshCommandService;

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {

        String cmd = String.format(BashCommands.DOCKER_PS);

        if (strings == null || strings.length == 0) {
            String resultCommand = sshCommandService.execCommandOnSelectServer(chat.getId(), cmd);
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user, resultCommand);
        }
    }
}
