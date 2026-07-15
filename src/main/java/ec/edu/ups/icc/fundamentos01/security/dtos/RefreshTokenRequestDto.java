package ec.edu.ups.icc.fundamentos01.security.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequestDto {

    @NotBlank(message = "El refresh token es obligatorio")
    private String refreshToken;

    public RefreshTokenRequestDto() {
    }

    public RefreshTokenRequestDto(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
