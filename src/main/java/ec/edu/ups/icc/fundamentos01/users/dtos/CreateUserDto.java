package ec.edu.ups.icc.fundamentos01.users.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class CreateUserDto {

    private String name;
    private String email;
    private String password;



}
