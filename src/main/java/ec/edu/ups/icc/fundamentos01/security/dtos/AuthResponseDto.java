package ec.edu.ups.icc.fundamentos01.security.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor

public class AuthResponseDto {

    private String token;

    private String type = "Bearer";

    private Long userId;

    private String name;

    private String email;

    private Set<String> roles;



    public AuthResponseDto(
            String token,
            Long userId,
            String name,
            String email,
            Set<String> roles
    ) {
        this.token = token;
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }
}
