package com.override.telegram_bot.commands;

import com.override.telegram_bot.enums.BashCommands;
import com.override.telegram_bot.enums.MessageContants;
import com.override.telegram_bot.service.SshCommandService;
import com.override.telegram_bot.service.TelegramUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.regex.Pattern;

import static com.override.telegram_bot.enums.Commands.LOGS;

@Component
public class DockerLogsCommand extends ServiceCommand {

    public DockerLogsCommand() {
        super(LOGS.getAlias(), LOGS.getDescription());
    }

    @Autowired
    private SshCommandService sshCommandService;

    @Autowired
    private TelegramUserService telegramUserService;

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (telegramUserService.isOwner(user)) {
            Pattern pattern = Pattern.compile("[0-9].*");
            String numLogs = Optional.ofNullable(strings)
                    .filter(str -> str.length == 2)
                    .map(str -> str[0])
                    .filter(str -> str.matches((pattern.pattern())))
                    .orElse("500");
            String dockerContainerName = Optional.ofNullable(strings)
                    .filter(str -> str.length == 2)
                    .filter(str -> !str[1].matches((pattern.pattern())))
                    .map(str -> str[1])
                    .orElse(strings.length == 1 ? strings[0] : null);
            if (dockerContainerName == null) {
                String msg = MessageContants.ERROR_LOGS_COMMAND;
                sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), user, msg);
                return;
            }
            String cmd = String.format(BashCommands.DOCKER_LOGS, numLogs, dockerContainerName);
            String resultCommand = sshCommandService.execCommandOnSelectServer(chat.getId(), cmd);
            InputStream stream = new ByteArrayInputStream(resultCommand.getBytes(StandardCharsets.UTF_8));

            SendDocument dock = new SendDocument();
            dock.setChatId(String.valueOf(chat.getId()));
            dock.setDocument(new InputFile(stream, "logs-" + dockerContainerName + ".txt"));

            try {
                absSender.execute(dock);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}
