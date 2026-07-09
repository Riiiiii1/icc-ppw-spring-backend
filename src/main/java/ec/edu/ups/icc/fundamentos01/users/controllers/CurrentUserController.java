package ec.edu.ups.icc.fundamentos01.users.controllers;
import ec.edu.ups.icc.fundamentos01.security.dtos.CurrentUserResponseDto;
import ec.edu.ups.icc.fundamentos01.security.services.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class CurrentUserController {
    @GetMapping("/me")
    public CurrentUserResponseDto me(
            @AuthenticationPrincipal UserDetailsImpl currentUser
    ) {
        Set<String> roles = currentUser.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        return new CurrentUserResponseDto(
                currentUser.getId(),
                currentUser.getName(),
                currentUser.getEmail(),
                roles
        );
    }
}
