package org.codenova.moneylog.Request;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FindPasswordRequest {

    @Email
    private String email;
}
