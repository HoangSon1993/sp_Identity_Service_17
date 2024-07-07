package sondev.indentityservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)

public class InvalidatedToken {
    @Id
    String id;
    // Căn cứ vào field này để xoá token, tránh DB phình to
    Date expiryTime;
}
