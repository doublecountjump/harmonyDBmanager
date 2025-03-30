package harmony.dbproject.domain;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_key",unique = true)
    private String key;
    private LocalDateTime expiredDate;

    public Token(UUID uuid){
        this.key = uuid.toString();
        this.expiredDate = LocalDateTime.now().plusMinutes(9);
    }
}
