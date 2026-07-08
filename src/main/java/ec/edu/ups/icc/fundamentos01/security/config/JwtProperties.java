package ec.edu.ups.icc.fundamentos01.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {
    private String secret;
    private Long expiration;
    private Long refreshExpiration;
    private String issuer;
    private String header;
    private String prefix;
}
