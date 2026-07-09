package ec.edu.ups.icc.fundamentos01.users.services;

import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.ConflictException;
import ec.edu.ups.icc.fundamentos01.core.exceptions.domain.NotFoundException;
import ec.edu.ups.icc.fundamentos01.users.dtos.CreateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.PartialUpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UpdateUserDto;
import ec.edu.ups.icc.fundamentos01.users.dtos.UserResponseDto;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.mappers.UserMapper;
import ec.edu.ups.icc.fundamentos01.users.models.UserModel;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 * Implementación del servicio de usuarios.
 *
 * En esta clase se reemplaza la lista en memoria por UserRepository.
 * El repositorio se encarga de comunicarse con PostgreSQL mediante JPA.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private  UserRepository userRepository;


    /*
     * Retorna todos los usuarios almacenados en PostgreSQL.
     *
     * El repositorio devuelve entidades.
     * El mapper convierte entidades a modelos.
     * Luego convierte modelos a DTOs de respuesta.
     */
    @Override
    public List<UserResponseDto> findAll() {

        return userRepository.findByDeletedFalse()
                .stream()
                .map(UserMapper::toModelFromEntity)
                .map(UserMapper::toResponse)
                .toList();
    }

    /*
     * Busca un usuario por id.
     *
     * Si no existe, lanza un error simple.
     * El manejo formal de errores se implementará después.
     */
    /*
     * Busca un usuario activo por id.
     *
     * Si no existe o está eliminado, lanza NotFoundException.
     */
    @Override
    public UserResponseDto findOne(Long id) {

        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("User not found");
        }

        UserModel model = UserMapper.toModelFromEntity(entity);

        return UserMapper.toResponse(model);
    }

    @Override
    public UserResponseDto create(CreateUserDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("Email already registered");
        }

        UserModel model = UserMapper.toModelFormDTO(dto);

        UserEntity entity = UserMapper.toEntityFromModel(model);
        entity.setPasswordHash("HASH_" + dto.getPassword());

        UserEntity savedEntity = userRepository.save(entity);

        UserModel savedModel = UserMapper.toModelFromEntity(savedEntity);

        return UserMapper.toResponse(savedModel);
    }


    @Override
    public UserResponseDto update(Long id, UpdateUserDto dto) {

        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("User not found");
        }
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
        entity.setPasswordHash("HASH_"+dto.getPassword());
        UserEntity savedEntity = userRepository.save(entity);

        UserModel model = UserMapper.toModelFromEntity(savedEntity);

        return UserMapper.toResponse(model);
    }

    /*
     * Actualiza parcialmente un usuario.
     *
     * Solo actualiza los campos enviados en el DTO.
     */
    @Override
    public UserResponseDto partialUpdate(Long id, PartialUpdateUserDto dto) {
        UserEntity entity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (entity.isDeleted()) {
            throw new NotFoundException("User not found");
        }

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }

        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null) {
            entity.setPasswordHash("HASH_" + dto.getPassword());
        }

        UserEntity savedEntity = userRepository.save(entity);

        UserModel model = UserMapper.toModelFromEntity(savedEntity);

        return UserMapper.toResponse(model);
    }

    /*
     * Elimina lógicamente un usuario por id.
     *
     * Primero verifica que exista.
     * Luego marca la entidad como eliminada usando deleted = true.
     * No elimina físicamente el registro de la base de datos.
     */
    @Override
    public void delete(Long id) {

        UserEntity user = userRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        user.setDeleted(true);
        userRepository.save(user);
    }
}