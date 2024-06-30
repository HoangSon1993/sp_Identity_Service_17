package sondev.indentityservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINT = {"/users",
            "/auth/log-in", "/auth/introspect"};

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINT).permitAll()
                        .anyRequest().authenticated());

        httpSecurity.oauth2ResourceServer(oath2 ->
                oath2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDeCoder())));

        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    org.springframework.security.oauth2.jwt.JwtDecoder jwtDeCoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HmacSHA512");
        // lưu ý lúc encode: JWSAlgorithm.HS512
        // decode "HmacSHA512"
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }


}
