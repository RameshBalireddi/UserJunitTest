package CustomUserJunit.SerivesTest;

import CustomUserJunit.APIResponse.APIResponse;
import CustomUserJunit.Config.ApplicationUser;
import CustomUserJunit.DTO.EmailRequest;
import CustomUserJunit.DTO.PasswordDTO;
import CustomUserJunit.DTO.UserDTO;
import CustomUserJunit.Entities.User;
import CustomUserJunit.Repositories.UserRepository;
import CustomUserJunit.Service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }
    @Test
    public void testAddUserSuccess() {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("John");
        userDTO.setEmail("ram1@gmail.com");
        userDTO.setPassword("Bunny1@");
        userDTO.setRole("USER");

        ResponseEntity<APIResponse> responseEntity = userService.addUser(userDTO);
        APIResponse apiResponse = responseEntity.getBody();
        System.out.print(apiResponse);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());


        assertNotNull(apiResponse);
        assertTrue(apiResponse.isSuccess());
        assertEquals("User added successfully", apiResponse.getMessage());
        User savedUser = userRepository.findByEmail(userDTO.getEmail());
        assertNotNull(savedUser);
        assertEquals(userDTO.getUserName(), savedUser.getName());
        assertEquals(userDTO.getEmail(), savedUser.getEmail());
        assertTrue(bCryptPasswordEncoder.matches(userDTO.getPassword(), savedUser.getPassword()));
    }

    @Test
    public void testAddUserFail() {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("John");
        userDTO.setEmail("ram1@gmail.com");
        userDTO.setPassword("Bunny1");
        userDTO.setRole("USER");

        ResponseEntity<APIResponse> responseEntity = userService.addUser(userDTO);
        APIResponse apiResponse = responseEntity.getBody();
        System.out.print(apiResponse);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

    }

    @Test
    public void testUserNotFound() {

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("John");
        userDTO.setEmail("ram1@gmail.com");
        userDTO.setPassword("Bunny1@");
        userDTO.setRole("USER");

        ResponseEntity<APIResponse> responseEntity = userService.addUser(userDTO);
        APIResponse apiResponse = responseEntity.getBody();
        System.out.print(apiResponse);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());


        assertNotNull(apiResponse);
        assertTrue(apiResponse.isSuccess());
        assertEquals("User added successfully", apiResponse.getMessage());

        User savedUser = userRepository.findByEmail("tset@gmail.com");
        assertNull(savedUser);

    }

    @Test
    public void testUpdateEmailByUserIdSuccess() {

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail("ram11@example.com");

         User user = new User();
//        user.setId(1);
        user.setName("Bose");
        user.setEmail("ram1@gmail.com");
        user.setRole("USER");
        user.setPassword("bunny1");
        userRepository.save(user);

        ApplicationUser testUser = new ApplicationUser(user);


        Authentication authentication = new UsernamePasswordAuthenticationToken(testUser, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);


        ResponseEntity<APIResponse> responseEntity = userService.updateEmailByUserId(emailRequest);
        APIResponse apiResponse = responseEntity.getBody();
        System.out.print(apiResponse);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertNotNull(apiResponse);
        assertTrue(apiResponse.isSuccess());
        User updatedUser = userRepository.findById(testUser.getUserId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals(emailRequest.getEmail(), updatedUser.getEmail());
    }

    @Test
    public void testDeleteUserByIdSuccess() {
        // Create a user in the database (replace with valid user data)
        User user = new User();
//        user.setId(1);
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("password");
        user.setRole("USER");
        userRepository.save(user);

        ApplicationUser testUser = new ApplicationUser(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(testUser, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResponseEntity<APIResponse> responseEntity = userService.deleteUserById(user.getId());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        APIResponse apiResponse = responseEntity.getBody();
        assertNotNull(apiResponse);
        assertEquals("User deleted successfully", apiResponse.getMessage());

        User deletedUser = userRepository.findById(user.getId()).orElse(null);
        assertNull(deletedUser);
    }

    @Test
    public void testUpdateUserPasswordSuccess() {
        // Create a user in the database (replace with valid user data)
        User user = new User();
//        user.setId(1);
        user.setName("John");
        user.setEmail("ram22@gmail.com");
        user.setRole("USER");
        user.setPassword(bCryptPasswordEncoder.encode("Bunny1@"));
        userRepository.save(user);

        ApplicationUser userDetails = new ApplicationUser(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
//       User authUser= (User) authentication.getPrincipal();
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setOldPassword("Bunny1@");
        passwordDTO.setNewPassword("Bunny11@");
        passwordDTO.setConfirmPassword("Bunny11@");

        ResponseEntity<APIResponse> responseEntity = userService.updateUserPassword(passwordDTO);
        APIResponse apiResponse = responseEntity.getBody();
        System.out.print(apiResponse);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertNotNull(apiResponse);
        assertEquals("Password updated successfully.", apiResponse.getMessage());

        User updatedUser = userRepository.findById(user.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertTrue(bCryptPasswordEncoder.matches(passwordDTO.getNewPassword(), updatedUser.getPassword()));
    }
}
