package ec.edu.ups.icc.fundamentos01.users.controllers;

import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos01.users.models.UserModel;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
@RestController
@RequestMapping("/api/users")
public class UserControllers {

    private final AtomicLong idContadorUsers = new AtomicLong(1);
    private List<UserModel> users = new ArrayList<>();


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
    public Object findOne(@PathVariable Long id) {
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
        // Asignar ID.
        user.setId(idContadorUsers.getAndIncrement());

        // Se agrega el niuevo usuario mapeado al array
        users.add(user);
        // Se retorna un response pasandole el user.
        return UserMapper.toResponse(user);
    }
    @PutMapping("/{id}")
    public Object update(@PathVariable int id, @RequestBody UpdateUserDto dto) {

        // Programacion tradicional iterativa
        for (UserModel user : users) {
           if (user.getId().equals(id)) {
                user.setName(dto.getName());
                user.setEmail(dto.getEmail());
               return UserMapper.toResponse(user);
            }
        }
        return new Object() {
            public String error = "UserModel not found";
      };

        // Programacion funcional
//        UserModel user = users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
//        if (user == null)
//            return new Object() {
//                public String error = "UserModel not found";
//            };
//
//        user.setName(dto.getName());
//        user.setEmail(dto.getEmail());
//
//        return UserMapper.toResponse(user);
    }


    @PatchMapping("/{id}")
    public Object partialUpdate(@PathVariable int id, @RequestBody PartialUpdateUserDto dto) {

//        // Programacion tradicional iterativa
//        for (UserModel user : users) {
//            // ESTE ES EL CAMBIO pero deberia estar en un metodo aparte para evitar
//            // duplicacion de codigo y mejorar mantenibilidad con separacion de
//            // responsabilidades.
//            if (user.getId().equals(id)) {
//                if (dto.getName() != null)
//                    user.setName(dto.getName());
//                if (dto.getEmail() != null)
//                    user.setEmail(dto.getEmail());
//                return UserMapper.toResponse(user);
//            }
//        }
//        return new Object() {
//            public String error = "UserModel not found";
//        };

        // Programación funcional
        // Búsqueda lineal del usuario por id
        UserModel user = users.stream().filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (user == null)
            return new Object() {
                public String error = "UserModel not found";
            };

        if (dto.getName() != null)
            user.setName(dto.getName());
        if (dto.getEmail() != null)
            user.setEmail(dto.getEmail());

        return UserMapper.toResponse(user);
    }
    @DeleteMapping("/{id}")
    public Object delete(@PathVariable int id) {

        // Programacion funcional
        boolean exists = users.removeIf(u -> u.getId().equals(id));
        if (!exists)
            return new Object() {
                public String error = "User not found";
            };

        return new Object() {
            public String message = "Deleted successfully";
        };
    }
}
