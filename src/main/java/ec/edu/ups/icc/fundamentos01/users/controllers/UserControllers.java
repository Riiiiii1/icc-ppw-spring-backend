package ec.edu.ups.icc.fundamentos01.users.controllers;

import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos01.users.models.UserModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserControllers {
    private List<UserModel> users = new ArrayList<>();
    private int currentId;

    @GetMapping
    public List<UserResponseDto> findAll() {
        // Logica tradicional iterativa
            //        List<UserResponseDto> dtos = new ArrayList<>();
            //        for (UserModel user : users) {
            //            dtos.add(UserMapper.toResponse(user));
            //        }
            //        return dtos;
        // La logica mas dura
        return users.stream().map(UserMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public Object findOne(@PathVariable int id) {
        // Logica tradicional iterativa
        //        for (UserModel user : users) {
        //            if(user.getId() == id) {
        //                return UserMapper.toResponse(user);
        //            }
        //        }
        //        return new Object() {
        //            public String error = "User not found";
        //        };


        // La logica mas dura
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                // Cada DTO convertido, se devuelve como Object
                .map(user -> (Object) UserMapper.toResponse(user))
                // En caso de error, se devuelve un objeto User Not Found.
                .orElseGet(() -> new Object() {
                    public String error = "User not found";
                });
    }

    @PostMapping
    // Entra un RequestBody tipo CreateUserDto
    public UserResponseDto create(@RequestBody CreateUserDto dto) {
        // El modelo user es igual al DTO mapeado por UserMapper
        UserModel user = UserMapper.toModel(dto);
        // Se agrega el niuevo usuario mapeado al array
        users.add(user);
        // Se retorna un response pasandole el user.
        return UserMapper.toResponse(user);
    }
}
