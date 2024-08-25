package com.override.telegram_bot.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Commands {
    START("start", "Начать использование бота"),
    HELP("help", "Помощь"),
    SERVERS("servers", "Показать список доступных серверов"),
    LOGS("logs", "Показать докер логи"),
    DOCKER("docker", "Показать имена докер контейнеров"),
    DOCKERS("dockers", "Показать имена докер контейнеров"),
    WEB("web", "Показать адрес веб админки");
    private String alias;
    private String description;

}
