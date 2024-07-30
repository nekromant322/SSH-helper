package com.override.telegram_bot.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "t_server")
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotEmpty(message = "Имя сервера не должно быть пустым!")
    @Size(min = 3, max = 20, message = "Имя сервера должно быть от 3 до 20 символов!")
    private String name;

    @Column
    @NotEmpty(message = "IP не должно быть пустым!")
    @Size(min = 7, max = 15, message = "IP сервера должно быть от 7 до 20 символов!")
    private String ip;

    @Transient
    @ManyToMany(mappedBy = "servers")
    private Set<User> users;

    public Server(Long id, String name, String ip) {
        this.id = id;
        this.name = name;
        this.ip = ip;
    }
}
