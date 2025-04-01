package org.codenova.moneylog.Request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class LoginRequest {
    private String email;
    private String password;
}
