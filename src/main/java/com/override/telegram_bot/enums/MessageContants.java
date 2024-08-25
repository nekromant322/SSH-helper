package com.override.telegram_bot.enums;

public class MessageContants {
    public static final String TELEGRAM_URL_INFO_FILE = "https://api.telegram.org/bot%s/getFile?file_id=%s";
    public static final String TELEGRAM_URL_DOWNLOAD_FILE = "https://api.telegram.org/file/bot%s/%s";
    public static final String URL_NOT_FOUND = "Не удалось получить ссылку для загрузки файла!";
    public static final String FILE_LOAD_AND_USER_CREAT = "Файл %s Загружен на сервер! %s 🚀 \nПользователь %s создан! ✅";
    public static final String FILE_LOAD_BUT_USER_NOT_CREAT = "Файл %s Загружен на сервер! 🚀 \nНо пользователь %s не был создан! ❌";
    public static final String FILE_NOT_LOAD = "Не удалось загрузить файл ❌";
    public static final String FILE_NOT_PUB_KEY = "Расширение файла не .pub! ❌";
    public static final String ERROR_USER_NAME = "Ошибка в имени пользователя! ❌";
    public static final String ERROR_FILE_NAME = "Ошибка в имени файла! ❌";
    public static final String USER_CREATE_IN_DB = "Пользователь - %s добавлен в БД ✅";
    public static final String USER_UPDATE_IN_DB = "Для пользователя - %s добавлен сервер %s ✅";
    public static final String ERROR_AUTH_TO_SERVER = "Не удалось аутентифицироваться на сервера! ❌";
    public static final String ERROR_EXEC_COMMAND_TO_REMOTE_SERVER = "Не удалось выполнить команду на удалённом сервере! ❌";
    public static final String ERROR_EXEC_COMMAND_TO_LOCAL_SERVER = "Не удалось выполнить команду на локальном сервере! ❌";
    public static final String ERROR_LOGS_COMMAND = "Неверная команда!❌ Пример: /logs <имя контейнера> или /logs <число строк логов> <имя контейнера>";
    public static final String START_TEXT = "Ку! Я могу добавить публичный SSH 🔑 на любой доступный сервер. Узнать доступные сервера /servers. Получить помощь /help";
    public static final String CHOSEN_SERVER = "Выбран сервер %s";
    public static final String WEB_URL = "http://%s:%s/login";
}
