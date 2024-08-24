package com.override.telegram_bot.model;

import lombok.*;
import org.telegram.telegrambots.meta.api.objects.Document;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
@AllArgsConstructor
@Table(name = "t_telegram_user")
public class TelegramUser {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    private Long id;
    private Long chatId;
    private String docFileName;
    private String docFileId;
    private String caption;
    private String serverIp;
}
