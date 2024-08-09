package com.override.telegram_bot.service;

import com.override.telegram_bot.enums.BashCommands;
import com.override.telegram_bot.enums.MessageContants;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

@Service
public class FileService {

    @Autowired
    private SshCommandService sshCommandService;

    @SneakyThrows
    private String getUrlFile(String file_id, String token) {
        String url = String.format(MessageContants.TELEGRAM_URL_INFO_FILE, token, file_id);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            String res = in.readLine();
            JSONObject result = new JSONObject(res);
            JSONObject path = result.getJSONObject("result");
            String file_path = path.getString("file_path");
            return String.format(MessageContants.TELEGRAM_URL_DOWNLOAD_FILE, token, file_path);
        }
    }

    public String executeLoadKeyFile(String serverIp, Document document, String newServerUser, String botToken) throws IllegalArgumentException {
        String docName = document.getFileName();
        if (isValidFile(docName)) {
            String docId = document.getFileId();
            String fileUrl = getUrlFile(docId, botToken);
            String wgetCommand = String.format(BashCommands.WGET_AND_CREATE_USER, docName, fileUrl, newServerUser);
            String resultCommand = sshCommandService.execCommand(serverIp, wgetCommand);
            if (isUserCreate(resultCommand)) {
                return String.format(MessageContants.FILE_LOAD_AND_USER_CREAT, docName, serverIp, newServerUser);
            }
            return String.format(MessageContants.FILE_LOAD_BUT_USER_NOT_CREAT, docName, newServerUser);
        } else return MessageContants.FILE_NOT_LOAD;
    }

    public boolean isValidFile(String docName) {
        if (docName != null) {
            String typeDoc = docName.substring(docName.lastIndexOf("."));
            if (typeDoc.equals(".pub")) {
                return true;
            }
            throw new IllegalArgumentException(MessageContants.FILE_NOT_PUB_KEY);
        }
        throw new IllegalArgumentException(MessageContants.ERROR_FILE_NAME);
    }

    private boolean isUserCreate(String string) {
        return string.contains("success"); //Строка для валидации успешного создания пользователя на сервере
    }
}

