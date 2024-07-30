package com.override.telegram_bot.enums;

public class BashCommands {
    public static final String WGET_AND_EXEC_SCRIPT = "wget -c -O \"%s\" \"%s\" && ./%s %s";
    public static final String DELUSER = "sudo deluser --remove-home %s";
    public static final String DOCKER_LOGS = "sudo tail -n %s `sudo docker inspect --format='{{.LogPath}}' %s`";
    public static final String DOCKER_PS = "sudo docker ps --format \"{{.Names}}\"";
    public static final String DOCKER_PS_ALL = "sudo docker ps -a --format \"{{.Names}}    -->    {{.Status}}\"";

}
