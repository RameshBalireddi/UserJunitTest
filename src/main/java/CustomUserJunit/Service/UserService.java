package CustomUserJunit.Service;

import CustomUserJunit.APIResponse.APIResponse;
import CustomUserJunit.Config.ObjectUtil;
import CustomUserJunit.DTO.EmailRequest;
import CustomUserJunit.DTO.PasswordDTO;
import CustomUserJunit.DTO.UserDTO;
import CustomUserJunit.Entities.User;
import CustomUserJunit.Repositories.UserRepository;
import CustomUserJunit.Response.UserResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public ResponseEntity<APIResponse> addUser(@Valid UserDTO userDTO) {
        User userEmail=userRepository.findByEmail(userDTO.getEmail());
        if(userEmail!=null){
            return  APIResponse.errorBadRequest("Email address is already registered. give unique email");
        }
        User user = new User();
        user.setName(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());


        if (!isValidPassword(userDTO.getPassword())) {
            return APIResponse.errorBadRequest("Password must contain at least one number, one capital letter, and one special character.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        APIResponse response = new APIResponse();
        response.setSuccess(true);
        response.setMessage("User added successfully");
        response.setData(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private boolean isValidPassword(String password) {
        // Regular expression to check for at least one number, one capital letter, and one special character
        String passwordRegex = "^(?=.*\\d)(?=.*[A-Z])(?=.*[!@#$%^&*]).*$";
        return password.matches(passwordRegex);
    }

    public ResponseEntity<APIResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return APIResponse.errorNotFound("users not found");
        }
        List<UserResponse> userResponses=users.stream()
                .map(u->new UserResponse(u)).collect(Collectors.toList());
        return APIResponse.success("users are : ", userResponses);
    }

    public ResponseEntity<APIResponse> deleteUserById(int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user==null) {
            return APIResponse.errorNotFound("user not found");
        }
        userRepository.delete(user);

        return APIResponse.success("User deleted successfully", user.getName());
    }
    public ResponseEntity<APIResponse> updateEmailByUserId(EmailRequest emailRequest) {
        try {
            User user = userRepository.findById(ObjectUtil.getUserId()).orElse(null);
            if (user == null) {
                return APIResponse.errorNotFound("user not found");
            }
            User email=userRepository.findByEmail(emailRequest.getEmail());
            if(email!=null){
                return  APIResponse.errorBadRequest("Email address is already registered. give unique email");
            }

            user.setEmail(emailRequest.getEmail());
            userRepository.save(user);

            return APIResponse.success("user email updated successfully ", user.getEmail());
        }catch (RuntimeException r){
            return APIResponse.errorBadRequest("please provide valid email");
        }
    }

    public ResponseEntity<APIResponse> updateUserPassword(PasswordDTO passwordDTO) {

            User userProfile = userRepository.findById(ObjectUtil.getUserId()).orElse(null);
            if (userProfile == null) {
                return APIResponse.errorNotFound("user not found");
            }
            if (!bCryptPasswordEncoder.matches(passwordDTO.getOldPassword(), userProfile.getPassword())) {
                return APIResponse.errorBadRequest("Old password is did not matched.");
            }
            if(!(passwordDTO.getNewPassword()).equals(passwordDTO.getConfirmPassword())){
                return  APIResponse.errorBadRequest("new password and confirm password not matched");
            }
            if (!isValidPassword(passwordDTO.getConfirmPassword())) {
                return APIResponse.errorBadRequest("Password must contain at least one number, one capital letter, and one special character.");
            }

            String encodedPassword = passwordEncoder.encode(passwordDTO.getConfirmPassword());
            userProfile.setPassword(encodedPassword);
            userRepository.save(userProfile); // Replace this with your saving logic
            return ResponseEntity.ok(APIResponse.success("Password updated successfully.", passwordDTO)).getBody();
         }
    }


