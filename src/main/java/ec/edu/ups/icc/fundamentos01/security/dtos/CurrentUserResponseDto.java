package ec.edu.ups.icc.fundamentos01.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserResponseDto {

    private Long id;

    private String name;

    private String email;

    private Set<String> roles;
}
