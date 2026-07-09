package ec.edu.ups.icc.fundamentos01.security.config;

import ec.edu.ups.icc.fundamentos01.security.entity.RoleEntity;
import ec.edu.ups.icc.fundamentos01.security.enums.RoleName;
import ec.edu.ups.icc.fundamentos01.security.repositories.RoleRepository;
import ec.edu.ups.icc.fundamentos01.users.entities.UserEntity;
import ec.edu.ups.icc.fundamentos01.users.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityDataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Inyecta el codificador real de Spring Security

    public SecurityDataInitializer(RoleRepository roleRepository,
                                   UserRepository userRepository,
                                   PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Asegura que los roles existan primero
        createRoleIfNotExists(RoleName.ROLE_USER, "Usuario estándar del sistema");
        createRoleIfNotExists(RoleName.ROLE_ADMIN, "Administrador del sistema");

        // Crea el administrador usando persistencia nativa de JPA
        createAdminUserIfNotExists();
    }

    private void createRoleIfNotExists(RoleName roleName, String description) {
        if (!roleRepository.existsByName(roleName)) {
            RoleEntity role = new RoleEntity(roleName, description);
            roleRepository.save(role);
        }
    }

    private void createAdminUserIfNotExists() {
        String adminEmail = "admin@ups.edu.ec";

        // Si ya existía un registro erróneo o viejo con ese correo, lo limpiamos para no chocar
        userRepository.findByEmail(adminEmail).ifPresent(user -> {
            userRepository.delete(user);
            userRepository.flush(); // Fuerza la eliminación inmediata en la BD
        });

        // Buscamos el rol de administrador recién creado/verificado
        RoleEntity adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Error: Rol ADMIN no encontrado"));

        // Construimos el objeto dejando que Hibernate maneje la BaseEntity
        UserEntity admin = new UserEntity();
        admin.setName("Admin UPS");
        admin.setEmail(adminEmail);
        admin.setPasswordHash(passwordEncoder.encode("admin123")); // Hash real compatible con tu login
        admin.getRoles().add(adminRole);

        userRepository.save(admin);
        System.out.println(">>> [INIT] El usuario administrador se ha generado correctamente mediante Hibernate.");
    }
}