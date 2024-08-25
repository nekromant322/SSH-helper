package com.override.telegram_bot.model;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;


@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@AllArgsConstructor
@RedisHash("TelegramUser")
public class TelegramUser implements Serializable {

    @Id
    private Long id;
    @Indexed
    private Long chatId;
    private String docFileName;
    private String docFileId;
    private String caption;
    private String serverIp;
}
