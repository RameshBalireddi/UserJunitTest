package CustomUserJunit.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.Where;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "my_seq_gen", sequenceName = "your_sequence_name", allocationSize = 1)
    private int id;

    @Column(name="name")
    @NotNull
    private  String name;

    @Email
    private String email;

    @NotNull
    private  String password;

    @NotNull
    private String role;

}
