package ec.edu.ups.icc.fundamentos01.users.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserModel {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private String password;
    private String passwordHash;


    private LocalDateTime updatedAt;

    private boolean deleted;


}
