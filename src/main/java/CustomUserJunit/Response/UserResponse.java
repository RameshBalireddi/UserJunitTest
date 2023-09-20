package CustomUserJunit.Response;

import CustomUserJunit.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UserResponse {

 private   int userId;

 private String name;

 private String email;

 private String role;

 public UserResponse(User user) {
  this.userId = user.getId();
  this.name = user.getName();
  this.email = user.getEmail();
  this.role= user.getRole();;
 }
}
