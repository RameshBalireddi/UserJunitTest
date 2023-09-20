package CustomUserJunit.Config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ObjectUtil {


    public static ApplicationUser getObjectUtil() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;

        if (authentication != null && authentication.getPrincipal() instanceof ApplicationUser) {
            ApplicationUser user = (ApplicationUser) authentication.getPrincipal();

            return user;
        }
        return null;
    }

    public static int getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof ApplicationUser) {
            int userId = ((ApplicationUser) authentication.getPrincipal()).getUserId();
            return userId;
        }
        return 0;
    }
}
