package CustomUserJunit.Config;

import CustomUserJunit.Entities.User;
import CustomUserJunit.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user == null) {
            throw new UsernameNotFoundException("user notFound");
        }
        return user.map(ApplicationUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found" + email));

    }

}

