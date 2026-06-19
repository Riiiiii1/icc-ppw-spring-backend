package ec.edu.ups.icc.fundamentos01.users.mappers;

import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.models.UserModel;

// Esta clase convierte entre modelos y dtos.
// El mapper evita que el controlador copie manualmente los campos entre CreateUserDto, userModel.

public class UserMapper {
    // Esta clase convierte el DTO en un UserModelo.
    public static UserModel toModel(CreateUserDto createUserDto) {
        UserModel userModel = new UserModel();
        userModel.setName(createUserDto.getName());
        userModel.setEmail(createUserDto.getEmail());
        userModel.setPassword(createUserDto.getPassword());
        return userModel;
    }

    //Convierte en un UserModel  a un UserResponseDTO
    public static UserResponseDto toResponse(UserModel model) {

        UserResponseDto response = new UserResponseDto();

        response.setId(model.getId());
        response.setName(model.getName());
        response.setEmail(model.getEmail());

        return response;
    }

}
