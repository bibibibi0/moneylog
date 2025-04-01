package org.codenova.moneylog.entity;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int id;
    @Email
    private String email;
    private String password;
    private String nickname;
    private String picture;
    private String provider;
    private String verified;
    private LocalDateTime createdAt;
    private String providerId;
}
