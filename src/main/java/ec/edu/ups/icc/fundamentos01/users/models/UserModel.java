package ec.edu.ups.icc.fundamentos01.users.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class UserModel {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private String password;
    private String passwordHash;

    public UserModel(Long id, String name, String email, String password, String passwordHash) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.passwordHash = passwordHash;
    }

}
