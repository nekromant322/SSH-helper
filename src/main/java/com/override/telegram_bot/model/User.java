package com.override.telegram_bot.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotEmpty(message = "Имя не должно быть пустым!")
    @Size(min = 3, max = 20 , message = "Имя должно быть от 3 до 20 символов!")
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Server> servers;

}
