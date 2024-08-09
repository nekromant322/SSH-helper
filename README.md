## Перед запуском:

- Установи JDK 17
* Idea -> Plugins -> Lombok -> Install
+ Idea -> Settings -> Build, Execution, Deployment -> Compiler -> Annotation Proccesor -> Enable annotation Proccesing

## Для локального запуска добавить переменных окружения (Environment variables):

- BOT_NAME={имя телеграм бота}
- BOT_TOKEN={токен бота}
- SSH_USER={имя юзера от имени которого будут выполняться команды на удалённом сервере, например: tg-bot}
- SSH_PRIVATE_KEY_PATH={путь до приватного ключа юзера, например: C:\Users\ilya\Desktop\id_rsa}
- DB_URL={URL Postgresql, например: jdbc:postgresql://localhost:5432/postgres}
- DB_USER={имя юзера Postgresql, например: postgres}
- DB_PASS={пароль юзераPostgresql, например: postgres}

WEB панель администрирования можно открыть по адресу: http://localhost:999/login
