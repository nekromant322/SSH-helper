package com.override.telegram_bot.enums;

public class BashCommands {
    public static final String WGET_AND_CREATE_USER = """
            wget -c -O "%s" "%s" &&
            new_user=%s &&
            sudo adduser "$new_user" --disabled-password --gecos "" &>/dev/null &&
            sudo usermod -aG sudo "$new_user" &&
            sudo mkdir /home/"$new_user"/.ssh &&
            sudo chown -R "$new_user":"$new_user" /home/"$new_user"/.ssh/ &&
            sudo chmod 777 /home/"$new_user"/.ssh &&
            sudo touch /home/"$new_user"/.ssh/authorized_keys &&
            sudo chown -R "$new_user":"$new_user" /home/"$new_user"/.ssh/authorized_keys &&
            sudo chmod 777 /home/"$new_user"/.ssh &&
            sudo chmod 777 /home/"$new_user"/.ssh/authorized_keys &&
            if sudo cat /home/tkk-bot/id_rsa.pub > /home/"$new_user"/.ssh/authorized_keys; then >&1 echo "success"; else  >&1 echo "error"; fi &&
            sudo rm id_rsa.pub &&
            sudo chmod 600 /home/"$new_user"/.ssh/authorized_keys &&
            sudo chmod 700 /home/"$new_user"/.ssh
            """;
    public static final String DELUSER = "sudo deluser --remove-home %s";
    public static final String DOCKER_LOGS = "sudo tail -n %s `sudo docker inspect --format='{{.LogPath}}' %s`";
    public static final String DOCKER_PS = "sudo docker ps --format \"{{.Names}}\"";
    public static final String DOCKER_PS_ALL = "sudo docker ps -a --format \"{{.Names}}    -->    {{.Status}}\"";
}
