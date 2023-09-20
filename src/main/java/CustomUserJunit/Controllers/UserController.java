package CustomUserJunit.Controllers;

import CustomUserJunit.APIResponse.APIResponse;
import CustomUserJunit.Config.ObjectUtil;
import CustomUserJunit.DTO.EmailRequest;
import CustomUserJunit.DTO.PasswordDTO;
import CustomUserJunit.DTO.UserDTO;
import CustomUserJunit.Repositories.UserRepository;
import CustomUserJunit.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userProfileService;

    @PostMapping("/signup")
    public ResponseEntity<APIResponse> addUser(@RequestBody @Valid UserDTO userDTO) {
        return userProfileService.addUser(userDTO);
    }

    @GetMapping("/list")
    public ResponseEntity<APIResponse> getUsers() {
        return userProfileService.getAllUsers();
    }

    @PutMapping("update/email")
    public ResponseEntity<APIResponse> updateUserEmail(@RequestBody @Valid EmailRequest emailRequest) {

        return userProfileService.updateEmailByUserId(emailRequest);
    }
    @DeleteMapping("")
    public ResponseEntity<APIResponse> deleteUser() {
        int userId = ObjectUtil.getUserId();
        return userProfileService.deleteUserById(userId);
    }

    @PutMapping("/update/password")
    public ResponseEntity<APIResponse> updatePassword(@RequestBody @Valid PasswordDTO passwordDTO){
        return userProfileService.updateUserPassword(passwordDTO);
    }
}