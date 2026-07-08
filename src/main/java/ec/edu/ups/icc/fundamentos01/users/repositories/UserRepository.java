package ec.edu.ups.icc.fundamentos01.users.repositories;

import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByIdAndDeletedFalse(Long id);

    List<UserEntity> findByDeletedFalse();

    boolean existsByIdAndDeletedFalse(Long id);


    Optional<UserEntity> findById(Long id);

    // ============== NUEVOS MÉTODOS PARA SEGURIDAD ==============

    // Buscar usuario por email (usado en login)
    Optional<UserEntity> findByEmailAndDeletedFalse(String email);

    // Verificar si email ya está registrado (usado en registro)
    boolean existsByEmail(String email);
}