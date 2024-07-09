package sondev.indentityservice.configuration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sondev.indentityservice.entity.Role;
import sondev.indentityservice.entity.User;
import sondev.indentityservice.repository.RoleRepository;
import sondev.indentityservice.repository.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    @ConditionalOnProperty(prefix = "spring", value = "datasource.driverClassName",
    havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()){
                var role = new HashSet<String>();
               // role.add(Role.ADMIN.name());
                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        //roles(role)
                        .build();
                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change it");
            }
        };
    }
}

