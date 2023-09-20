package CustomUserJunit.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {

    @Size(min = 3, message = "Username must contain at least 3 characters")
    @Pattern(regexp = "^[a-zA-Z].*", message = "Username must start with atLest 3 characters")
    @NotNull(message = "user name must not be null")
    private String userName;

     @Email(message = "Invalid email format please enter valid email")
     private String email;

     @NotNull
     private String password;

     @NotNull
     private String role;
}
