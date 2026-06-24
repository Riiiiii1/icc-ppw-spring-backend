package ec.edu.ups.icc.fundamentos01.users.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
/*
 * DTO utilizado para recibir los datos que se desean
 * actualizar parcialmente en un usuario existente.
 *
 * Los campos pueden venir nulos cuando no se desean actualizar.
 * Solo se validan los campos enviados.
 */
public class PartialUpdateUserDto {

    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String name;

    @Email(message = "Debe ingresar un email válido")
    @Size(max = 150, message = "El email no debe superar los 150 caracteres")
    private String email;

    // Constructor vacío

    // Constructor lleno

    // Getters y setters
}